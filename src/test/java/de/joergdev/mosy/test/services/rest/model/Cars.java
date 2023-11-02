package de.joergdev.mosy.test.services.rest.model;

import java.util.ArrayList;
import java.util.List;

public class Cars
{
  private List<Car> cars = new ArrayList<>();

  public List<Car> getCars()
  {
    return cars;
  }

  public void setCars(List<Car> cars)
  {
    this.cars = cars;
  }
}
