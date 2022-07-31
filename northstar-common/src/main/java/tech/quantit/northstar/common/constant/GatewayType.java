package tech.quantit.northstar.common.constant;


public enum GatewayType {
	
	/**
	 * CTP生产
	 */
	CTP(new GatewayUsage[]{GatewayUsage.MARKET_DATA, GatewayUsage.TRADE}, false),
	/**
	 * CTP仿真
	 */
	CTP_SIM(new GatewayUsage[]{GatewayUsage.MARKET_DATA, GatewayUsage.TRADE}, true),
	/**
	 * 回放
	 */
	PLAYBACK(new GatewayUsage[]{GatewayUsage.MARKET_DATA}, false),
	/**
	 * 本地模拟
	 */
	SIM(new GatewayUsage[]{GatewayUsage.MARKET_DATA, GatewayUsage.TRADE}, false);
	
	
	private GatewayUsage[] usage;
	private boolean adminOnly;
	private GatewayType(GatewayUsage[] usage, boolean adminOnly) {
		this.usage = usage;
		this.adminOnly = adminOnly;
	}
	public GatewayUsage[] getUsage() {
		return usage;
	}
	public boolean isAdminOnly() {
		return adminOnly;
	}
}
