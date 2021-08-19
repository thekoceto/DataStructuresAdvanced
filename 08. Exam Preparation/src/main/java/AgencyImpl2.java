import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AgencyImpl2 implements Agency {

    Map<LocalDate, Map<String, Invoice>> invoices;
    Map<String, Invoice> allInvoices;
    Set<Invoice> payed;

    public AgencyImpl2() {
        this.invoices = new HashMap<>();
        this.allInvoices = new HashMap<>();
        this.payed = new HashSet<>();
    }

    @Override
    public void create(Invoice invoice) {

        if (!this.allInvoices.containsKey(invoice.getNumber())) {
            this.invoices.putIfAbsent(invoice.getDueDate(), new TreeMap<>());
            this.allInvoices.putIfAbsent(invoice.getNumber(), invoice);
            this.invoices.get(invoice.getDueDate()).put(invoice.getNumber(), invoice);
            return;
        }

        throw new IllegalArgumentException();
    }

    @Override
    public boolean contains(String number) {
        return this.allInvoices.containsKey(number);
    }

    @Override
    public int count() {
        return this.allInvoices.size();
    }

    @Override
    public void payInvoice(LocalDate dueDate) {
        if (this.invoices.containsKey(dueDate)) {
            for (Invoice value : this.invoices.get(dueDate).values()) {
                value.setSubtotal(0);
                payed.add(value);
            }
            return;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void throwInvoice(String number) {
        if (this.contains(number)) {
            Invoice invoice = this.allInvoices.remove(number);
            this.invoices.get(invoice.getDueDate()).remove(number);
            return;
        }

        throw new IllegalArgumentException();
    }

    @Override
    public void throwPayed() {
        for (Invoice inv : this.payed) {
            Invoice invoice = this.allInvoices.remove(inv.getNumber());
            this.invoices.get(invoice.getDueDate()).remove(invoice.getNumber());
        }
        this.payed.clear();
    }

    @Override
    public Iterable<Invoice> getAllInvoiceInPeriod(LocalDate startDate, LocalDate endDate) {
        return allInvoices.values().stream().filter(x -> Objects.requireNonNull(x).getIssueDate()
                .isAfter(startDate.minusDays(1L)) &&
                x.getIssueDate().isBefore(endDate.plusDays(1L))).sorted((o1, o2) -> {
            if (o1.getIssueDate().compareTo(o2.getIssueDate()) == 0) {
                return o1.getDueDate().compareTo(o2.getDueDate());
            }
            return o1.getIssueDate().compareTo(o2.getIssueDate());
        }).collect(Collectors.toList());


    }

    @Override
    public Iterable<Invoice> searchByNumber(String number) {
        Collection<Invoice> filtered =allInvoices.values().stream().filter(x -> Objects.requireNonNull(x).getNumber().contains(number)).collect(Collectors.toList());

        if (filtered.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return filtered;
    }

    @Override
    public Iterable<Invoice> throwInvoiceInPeriod(LocalDate startDate, LocalDate endDate) {

        List<Invoice> invoices = allInvoices.values().stream().filter(
                x -> Objects.requireNonNull(x).getDueDate()
                        .isAfter(startDate) &&
                        x.getDueDate().isBefore(endDate)).collect(Collectors.toList());
        if (invoices.isEmpty()) {
            throw new IllegalArgumentException();
        }
        invoices.forEach(invoice -> {
            this.invoices.get(invoice.getDueDate()).remove(invoice.getNumber());
            this.allInvoices.remove(invoice.getNumber());
            this.payed.remove(invoice);
        });
        return invoices;
    }

    @Override
    public Iterable<Invoice> getAllFromDepartment(Department department) {
        return allInvoices.values().stream().filter(x -> Objects.requireNonNull(x)
                .getDepartment()
                .compareTo(department) == 0).sorted((a, b) -> {
            if (Double.compare(a.getSubtotal(), b.getSubtotal()) == 0) {
                return a.getIssueDate().compareTo(b.getIssueDate());
            }
            return Double.compare(b.getSubtotal(), a.getSubtotal());
        }).collect(Collectors.toList());
    }

    @Override
    public Iterable<Invoice> getAllByCompany(String companyName) {
        return allInvoices.values().stream().filter(x -> Objects.requireNonNull(x)
                .getCompanyName()
                .compareTo(companyName) == 0).sorted((a, b) -> b.getNumber().compareTo(a.getNumber())).collect(Collectors.toList());
    }

    @Override
    public void extendDeadline(LocalDate endDate, int days) {

        if (!this.invoices.containsKey(endDate)) throw new IllegalArgumentException();

        Set<Map.Entry<String, Invoice>> entries = this.invoices.get(endDate).entrySet();

        for (Map.Entry<String, Invoice> entry : entries) {
            entry.getValue().setDueDate(entry.getValue().getDueDate().plusDays(days));
        }
    }
}
