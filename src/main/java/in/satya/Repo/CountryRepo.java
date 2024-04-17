package in.satya.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.satya.entity.CountryEntity;

public interface CountryRepo  extends JpaRepository<CountryEntity, Integer>{

}
