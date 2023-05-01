package za.ac.cput.factory;
import za.ac.cput.model.Inventory;
import za.ac.cput.utils.StringHelper;

public class InventoryFactory {

    public static Inventory createInventory(Long inventoryID, String tabletStockAmount,
                                            String medicineStockAmount) {
        if(StringHelper.isNullorEmpty(tabletStockAmount)
                || StringHelper.isNullorEmpty(medicineStockAmount))
            throw new IllegalArgumentException("TabletStockAmount or MedicineStockAmount is null or empty");

        return new Inventory.Builder().setInventoryID(inventoryID)
                .setTabletStockAmount(tabletStockAmount)
                .setMedicineStockAmount(medicineStockAmount)
                .build();
    }
}
