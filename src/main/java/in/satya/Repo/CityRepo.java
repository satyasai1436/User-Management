package in.satya.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.satya.entity.CityEntity;

public interface CityRepo extends JpaRepository<CityEntity, Integer>{
	
	@Query(value = "select * from city_master where state_id=:stateId", nativeQuery = true)
	public List<CityEntity> getCities(Integer stateId);

}
