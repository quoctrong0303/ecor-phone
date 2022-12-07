package com.spring.quoctrong.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.quoctrong.models.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer>{

}
