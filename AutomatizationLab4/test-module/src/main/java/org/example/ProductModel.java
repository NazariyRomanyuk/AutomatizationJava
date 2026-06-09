package org.example;

@AutoProperties(notNull = true)
public class ProductModel
{
    protected long id;
    protected long categoryId;
    protected String name;
    protected String characteristics;
    protected String manufacturer;

    public ProductModel(long id, long categoryId, String name, String characteristics, String manufacturer) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.characteristics = characteristics;
        this.manufacturer = manufacturer;
    }

    public String nameExtension(@NotNullOrEmpty String extension) {
        return name + " " + extension;
    }

}
