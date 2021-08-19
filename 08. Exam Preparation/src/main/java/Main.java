import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        Agency agency = new AgencyImpl();
        Agency agency2 = new AgencyImpl2();

        for (int i = 1; i < 5; i++) {
            agency.create(new Invoice(i + "", "HRS", i + 125D, Department.INCOMES,
                    LocalDate.of(2018, 2, i),
                    LocalDate.of(2018, 3, i)));

            agency2.create(new Invoice(i + "", "HRS", i + 125D, Department.INCOMES,
                    LocalDate.of(2018, 2, i),
                    LocalDate.of(2018, 3, i)));
        }
//        System.out.println(agency.count());
//        System.out.println(agency2.count());

        System.out.print("agency1:");
        agency.searchByNumber("1").forEach(i -> System.out.print(i.getNumber() + ": " + i.getSubtotal() + ", "));

        System.out.println();
        System.out.print("agency2:");
        agency2.searchByNumber("1").forEach(i -> System.out.print(i.getNumber() + ": " + i.getSubtotal() + ", "));


        agency.payInvoice(LocalDate.of(2018, 3, 1));
        agency2.payInvoice(LocalDate.of(2018, 3, 1));

//        agency.throwInvoice("1");
//        agency2.throwInvoice("1");

        System.out.println();
        System.out.println("---------------------------");
        System.out.print("agency1:");
        agency.searchByNumber("1")
                .forEach(i -> System.out.print(i.getNumber() + ": " + i.getSubtotal() + ", "));

        System.out.println();
        System.out.print("agency2:");
        agency2.searchByNumber("1")
                .forEach(i -> System.out.print(i.getNumber() + ": " + i.getSubtotal() + ", "));

        agency.throwPayed();
        agency2.throwPayed();

//        agency.extendDeadline(LocalDate.of(2018, 3, 1), 100);
//        agency2.extendDeadline(LocalDate.of(2018, 3, 1), 100);

        System.out.println();
        System.out.println("---------------------------");
        System.out.print("agency1:");
        agency.searchByNumber("1")
                .forEach(i -> System.out.print(i.getNumber() + ": " + i.getSubtotal() + ", "));

        System.out.println();
        System.out.print("agency2:");
        agency2.searchByNumber("1")
                .forEach(i -> System.out.print(i.getNumber() + ": " + i.getSubtotal() + ", "));
    }
}
