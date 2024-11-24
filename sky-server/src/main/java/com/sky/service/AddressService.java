package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressService {
    List<AddressBook> getAddress();

    AddressBook getDefaultAddress();

    void save(AddressBook addressBook);

    AddressBook getById(Long id);

    void delete(Long id);

    void setDefault(AddressBook addressBook);

    void update(AddressBook addressBook);
}
