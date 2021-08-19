import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MicrosystemImpl implements Microsystem {

    private Map<Integer, Computer> computers;

    public MicrosystemImpl() {
        this.computers = new HashMap<>();
    }

    @Override
    public void createComputer(Computer computer) {
        if (this.computers.putIfAbsent(computer.getNumber(), computer) != null)
            throw new IllegalArgumentException("There is already a computer with that number.");
    }

    @Override
    public boolean contains(int number) {
        return this.computers.containsKey(number);
    }

    @Override
    public int count() {
        return this.computers.size();
    }

    @Override
    public Computer getComputer(int number) {
        Computer computer = this.computers.get(number);

        if (computer != null)
            return computer;

        throw new IllegalArgumentException("There is no computer with that number.");
    }

    @Override
    public void remove(int number) {
        if (this.computers.remove(number) == null)
            throw new IllegalArgumentException("There is no computer with that number.");
    }

    @Override
    public void removeWithBrand(Brand brand) {
        if (!this.computers.values().removeIf(computer -> computer.getBrand().compareTo(brand) == 0))
            throw new IllegalArgumentException("There is no computer with that brand.");
    }

    @Override
    public void upgradeRam(int ram, int number) {
        Computer computer = this.computers.get(number);

        if (computer == null)
            throw new IllegalArgumentException("There is no computer with that number.");

        if (computer.getRAM() < ram)
            computer.setRAM(ram);
    }

    @Override
    public Iterable<Computer> getAllFromBrand(Brand brand) {
        return this.computers.values()
                .stream()
                .filter(computer -> computer.getBrand().compareTo(brand) ==  0)
                .sorted((c1, c2) -> Double.compare(c2.getPrice(), c1.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Computer> getAllWithScreenSize(double screenSize) {
        return this.computers.values()
                .stream()
                .filter(computer -> Double.compare(computer.getScreenSize(),screenSize) == 0)
                .sorted((c1, c2) -> Integer.compare(c2.getNumber(), c1.getNumber()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Computer> getAllWithColor(String color) {
        return this.computers.values()
                .stream()
                .filter(computer -> computer.getColor().compareTo(color) == 0)
                .sorted((c1, c2) -> Double.compare(c2.getPrice(), c1.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Computer> getInRangePrice(double minPrice, double maxPrice) {
        return this.computers.values()
                .stream()
                .filter(computer -> computer.getPrice() >= minPrice &&
                                    computer.getPrice() <= maxPrice)
                .sorted((c1, c2) -> Double.compare(c2.getPrice(), c1.getPrice()))
                .collect(Collectors.toList());
    }

}
