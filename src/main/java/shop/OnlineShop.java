package shop;

import bank.Bank;
import main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OnlineShop {
    private final Bank bank;
    private final Person shopOwner;
    private final List<ShopItem> availableItems = new ArrayList<>(List.of(
            new ShopItem("Spezi", 20),
            new ShopItem("Cola", 30),
            new ShopItem("Sprite", 25)
    ));
    private final HashMap<Person, List<ShopItem>> shoppingCarts = new HashMap<>();

    public OnlineShop(Bank bank, Person shopOwner) {
        this.bank = bank;
        this.shopOwner = shopOwner;
    }

    public List<ShopItem> getAvailableItems() {
        return availableItems;
    }

    public void addToCart(Person buyer, ShopItem item){
        shoppingCarts.computeIfAbsent(buyer, k -> new ArrayList<>()).add(item);
    }

    public boolean placeOrder(Person buyer) {
        List<ShopItem> cart = shoppingCarts.get(buyer);
        if (cart == null || cart.isEmpty()) {
            return false;
        }

        double total = cart.stream().mapToDouble(ShopItem::getPrice).sum();

        boolean success = bank.transfer(buyer, shopOwner, total);
        if (!success) {
            return false;
        }
        
        cart.clear();

        return true;
    }
}
