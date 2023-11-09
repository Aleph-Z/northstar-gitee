package org.dromara.northstar.gateway.tiger;

import java.time.LocalDate;
import java.util.List;

import org.dromara.northstar.common.IDataServiceManager;

import xyz.redtorch.pb.CoreEnum.ExchangeEnum;
import xyz.redtorch.pb.CoreField.BarField;
import xyz.redtorch.pb.CoreField.ContractField;

public class TigerDataServiceManager implements IDataServiceManager{

	@Override
	public List<BarField> getMinutelyData(ContractField contract, LocalDate startDate, LocalDate endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BarField> getQuarterlyData(ContractField contract, LocalDate startDate, LocalDate endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BarField> getHourlyData(ContractField contract, LocalDate startDate, LocalDate endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BarField> getDailyData(ContractField contract, LocalDate startDate, LocalDate endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LocalDate> getHolidays(ExchangeEnum exchange, LocalDate startDate, LocalDate endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContractField> getAllContracts(ExchangeEnum exchange) {
		// TODO Auto-generated method stub
		return null;
	}

}
