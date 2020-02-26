package kz.nic.nc;

import kz.nic.nc.core.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRespository extends JpaRepository<Client, Long> {

}
