package com.devsuperior.dsmeta.services;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SaleReportDTO> getReports(String minDate, String maxDate, String name, Pageable pageable) {
		LocalDate max = parseMaxDate(maxDate);
		LocalDate min = parseMinDate(minDate, max);

		return repository.findAllReports(min, max, name, pageable);
	}

    public Page<SaleSummaryDTO> getSummaries(String minDate, String maxDate, Pageable pageable) {
		LocalDate max = parseMaxDate(maxDate);
		LocalDate min = parseMinDate(minDate, max);

		return repository.findAllSummaries(min, max, pageable);
    }

	private LocalDate parseMaxDate(String date) {
		if (Strings.isBlank(date)) {
			return LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		}

		return LocalDate.parse(date);
	}

	private LocalDate parseMinDate(String date, LocalDate maxDate) {
		if (Strings.isBlank(date)) {
			return maxDate.minusYears(1L);
		}

		return LocalDate.parse(date);
	}

}
