import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AgencyImpl implements Agency {
    private Map<String, Invoice> invoices;
    private List<String> payed;

    public AgencyImpl() {
        this.invoices = new HashMap<>();
        this.payed = new ArrayList<>();
    }

    @Override
    public void create(Invoice invoice) {
        if (this.invoices.putIfAbsent(invoice.getNumber(), invoice) != null)
            throw new IllegalArgumentException("There is already a invoice with that number.");

    }

    @Override
    public boolean contains(String number) {
        return this.invoices.containsKey(number);
    }

    @Override
    public int count() {
        return this.invoices.size();
    }

    @Override
    public void payInvoice(LocalDate dueDate) {
        int[] count = new int[1];

        this.invoices.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getDueDate().isEqual(dueDate))
                .forEach(entry -> {
                    entry.getValue().setSubtotal(0);
                    this.payed.add(entry.getKey());
                    count[0]++;
                });

        if (count[0] == 0)
            throw new IllegalArgumentException("There is no invoice with that Date.");
    }

    @Override
    public void throwInvoice(String number) {
        if (this.invoices.remove(number) == null)
            throw new IllegalArgumentException("There is no invoice with that number.");
    }

    @Override
    public void throwPayed() {
        for (String number : this.payed)
            this.invoices.remove(number);

        this.payed.clear();
    }

    @Override
    public Iterable<Invoice> getAllInvoiceInPeriod(LocalDate startDate, LocalDate endDate) {
        return this.invoices.values()
                .stream()
                .filter(invoice -> invoice.getIssueDate().isAfter(startDate.minusDays(1L)) &&
                                   invoice.getIssueDate().isBefore(endDate.plusDays(1L)))
                .sorted((i1, i2) -> {
                    if (i1.getIssueDate().compareTo(i2.getIssueDate()) == 0)
                        return i1.getDueDate().compareTo(i2.getDueDate());
                    return i1.getDueDate().compareTo(i2.getDueDate());
                })
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Invoice> searchByNumber(String number) {

        Collection<Invoice> result = this.invoices.values()
                .stream()
                .filter(invoice -> invoice.getNumber().contains(number))
                .collect(Collectors.toList());

        if (result.isEmpty())
            throw new IllegalArgumentException("There is no invoices .");

        return result;
    }

    @Override
    public Iterable<Invoice> throwInvoiceInPeriod(LocalDate startDate, LocalDate endDate) {
        Collection<Invoice> toRemove = new ArrayList<>();

        for(Iterator<Map.Entry<String, Invoice>> it = this.invoices.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Invoice> entry = it.next();
            if(entry.getValue().getDueDate().isAfter(startDate) && entry.getValue().getDueDate().isBefore(endDate)) {
                toRemove.add(entry.getValue());
                it.remove();
            }
        }

        if (toRemove.isEmpty() )
            throw new IllegalArgumentException("There is no invoices .");

        return toRemove;

    }

    @Override
    public Iterable<Invoice> getAllFromDepartment(Department department) {
        return this.invoices.values()
                .stream()
                .filter(invoice -> invoice.getDepartment().compareTo(department) == 0)
                .sorted((i1, i2) -> {
                    if (Double.compare(i2.getSubtotal(), i1.getSubtotal()) == 0)
                        return i1.getIssueDate().compareTo(i2.getIssueDate());
                    return Double.compare(i2.getSubtotal(), i1.getSubtotal());
                })
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Invoice> getAllByCompany(String companyName) {
        return this.invoices.values()
                .stream()
                .filter(invoice -> invoice.getCompanyName().equals(companyName))
                .sorted((i1, i2) -> i2.getNumber().compareTo(i1.getNumber()))
                .collect(Collectors.toList());
    }

    @Override
    public void extendDeadline(LocalDate endDate, int days) {
        int[] count = new int[1];

        this.invoices.values()
                .stream()
                .filter(invoice -> invoice.getDueDate().isEqual(endDate))
                .forEach(invoice -> {
                    invoice.setDueDate(invoice.getDueDate().plusDays(days));
                    count[0]++;
                });

        if (count[0] == 0)
            throw new IllegalArgumentException("There is no invoices.");
    }

}
