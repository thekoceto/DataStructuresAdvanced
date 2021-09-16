import java.util.*;
import java.util.stream.Collectors;


public class RoyaleArena implements IArena {
    private Set<Battlecard> battleCards;
    private Map<Integer, Battlecard> dictBattleCards;
    private Map<CardType, List<Battlecard>> battleTypes;

    public RoyaleArena() {
        this.battleCards = new LinkedHashSet<>();
        this.dictBattleCards = new LinkedHashMap<Integer, Battlecard>();
        this.battleTypes = new LinkedHashMap<CardType, List<Battlecard>>();
    }

    public int count() {
        return this.battleCards.size();
    }

    public void add(Battlecard card) {
        this.battleCards.add(card);
        this.dictBattleCards.put(card.getId(), card);
        if (!this.battleTypes.containsKey(card.getType())) {
            this.battleTypes.put(card.getType(), new ArrayList<>());
        }
        this.battleTypes.get(card.getType()).add(card);
    }

    public void changeCardType(int id, CardType type) {
        if (!this.dictBattleCards.containsKey(id)) {
            throw new IllegalArgumentException();
        }

        this.dictBattleCards.get(id).setType(type);
    }

    public boolean contains(Battlecard card) {
        return this.battleCards.contains(card);
    }

    public Iterable<Battlecard> findFirstLeastSwag(int n) {
        if (n > this.battleCards.size()) {
            throw new UnsupportedOperationException();
        }

        return this.battleCards.stream()
                .sorted(Comparator.comparingDouble(f -> f.getSwag()))
                .limit(n)
                .sorted(Comparator.comparingInt(x -> x.getId()))
                .collect(Collectors.toList());
    }

    public Iterable<Battlecard> getAllByNameAndSwag() {
        Map<String, Set<Battlecard>> cards = new HashMap<>();

        for (Battlecard battleCard : battleCards) {
            if (!cards.containsKey(battleCard.getName())) {
                cards.put(battleCard.getName(), new HashSet<>());
            }
            cards.get(battleCard.getName()).add(battleCard);
        }

        List<Battlecard> battlecards = new ArrayList<>();

        for (var entry : cards.entrySet()) {
            battlecards.add(entry.getValue().stream().max(Comparator.comparingDouble(Battlecard::getSwag)).orElse(null));
        }

        return battlecards.stream().filter(Objects::nonNull).sorted(Comparator.comparingDouble(Battlecard::getSwag)).collect(Collectors.toList());
    }


    public Iterable<Battlecard> getAllInSwagRange(double lo, double hi) {
        List<Battlecard> result;
        result = this.battleCards.stream().filter(s -> s.getSwag() >= lo && s.getSwag() <= hi).sorted(Comparator.comparingDouble(x -> x.getSwag())).collect(Collectors.toList());

        return result;
    }

    public Iterable<Battlecard> getByCardType(CardType type) {

        if (!this.battleTypes.containsKey(type)) {
            throw new UnsupportedOperationException();
        }
        var result = this.battleTypes.get(type);
        return result.stream().sorted(Battlecard::compareTo).collect(Collectors.toList());
    }

    public Iterable<Battlecard> getByCardTypeAndMaximumDamage(CardType type, double damage) {
        if (!this.battleTypes.containsKey(type)) {
            throw new UnsupportedOperationException();
        }

        var result = this.battleTypes.get(type).stream().filter(v -> v.getDamage() <= damage).sorted(Battlecard::compareTo).collect(Collectors.toList());

        if (result.size() == 0) {
            throw new UnsupportedOperationException();
        }

        return result;
    }

    public Battlecard getById(int id) {
        if (!this.dictBattleCards.containsKey(id)) {
            throw new UnsupportedOperationException();
        }

        return this.dictBattleCards.get(id);
    }

    public Iterable<Battlecard> getByNameAndSwagRange(String name, double lo, double hi) {
        var result = this.battleCards
                .stream()
                .filter(x -> x.getName().equals(name) && x.getDamage() >= lo && x.getDamage() < hi)
                .sorted((f, s) -> Double.compare(s.getSwag(), f.getSwag())).sorted(Comparator.comparingInt(x -> x.getId())).collect(Collectors.toList());
        if (result.size() == 0) {
            throw new UnsupportedOperationException();
        }
        return result;
    }

    public Iterable<Battlecard> getByNameOrderedBySwagDescending(String name) {
        var result = this.battleCards
                .stream()
                .filter(x -> x.getName().equals(name))
                .sorted(Comparator.comparingDouble(Battlecard::getSwag).reversed().thenComparing(Battlecard::getId)).collect(Collectors.toList());

        if (result.size() == 0) {
            throw new UnsupportedOperationException();
        }
        return result;
    }

    public Iterable<Battlecard> getByTypeAndDamageRangeOrderedByDamageThenById(CardType type, int lo, int hi) {
        if (!this.battleTypes.containsKey(type)) {
            throw new UnsupportedOperationException();
        }
        var result = this.battleTypes.get(type)
                .stream()
                .filter(v -> v.getDamage() > lo && v.getDamage() < hi)
                .sorted(Battlecard::compareTo).collect(Collectors.toList());

        if (result.size() == 0) {
            throw new UnsupportedOperationException();
        }

        return result;

    }

    public void removeById(int id) {
        if (!this.dictBattleCards.containsKey(id)) {
            throw new UnsupportedOperationException();
        }
        var toRemove = this.dictBattleCards.get(id);
        this.dictBattleCards.remove(id);
        this.battleCards.remove(toRemove);

        this.battleTypes.get(toRemove.getType()).remove(toRemove);
    }

    @Override
    public Iterator<Battlecard> iterator() {
        return this.dictBattleCards.values().iterator();
    }
}
