package de.joergdev.mosy.test.services.rest.model;

public class Subpart implements TestableModel
{
  private Integer id;
  private String name;
  private Integer version;

  public Subpart()
  {

  }

  public Subpart(String type, int age)
  {
    this(null, type, age);
  }

  public Subpart(Integer id, String name, Integer version)
  {
    this.id = id;
    this.setName(name);
    this.setVersion(version);
  }

  public Integer getId()
  {
    return id;
  }

  public void setId(Integer id)
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public Integer getVersion()
  {
    return version;
  }

  public void setVersion(Integer version)
  {
    this.version = version;
  }
}