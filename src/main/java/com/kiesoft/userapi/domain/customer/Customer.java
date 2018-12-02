package com.kiesoft.userapi.domain.customer;

import java.util.List;

public interface Customer<T>  {

    String getFirstName();

    void setFirstName(String firstname);

    String getLastName();

    void setLastName(String lastName);

    List<T> getAliases();

    void setAliases(List<T> aliases);

    void addAlias(T alias);

    void removeAlias(T alias);

}
