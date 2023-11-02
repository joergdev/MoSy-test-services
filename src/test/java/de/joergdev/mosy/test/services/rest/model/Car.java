package de.joergdev.mosy.test.services.rest.model;

public class Car implements TestableModel
{
  private Integer id;
  private String type;
  private int age;

  public Car()
  {

  }

  public Car(String type, int age)
  {
    this(null, type, age);
  }

  public Car(Integer id, String type, int age)
  {
    this.id = id;
    this.type = type;
    this.age = age;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId(Integer id)
  {
    this.id = id;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public int getAge()
  {
    return age;
  }

  public void setAge(int age)
  {
    this.age = age;
  }
}