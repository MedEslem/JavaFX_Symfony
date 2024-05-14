package services;

import models.Product;

public interface OnChangeListener {
    public void onSupprimerClicked();
    public void onModdifierClicked(Product c);
}