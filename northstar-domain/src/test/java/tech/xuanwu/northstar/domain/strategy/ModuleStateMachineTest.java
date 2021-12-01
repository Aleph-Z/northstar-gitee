package tech.xuanwu.northstar.domain.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tech.xuanwu.northstar.strategy.api.StateChangeListener;
import tech.xuanwu.northstar.strategy.api.constant.ModuleState;
import tech.xuanwu.northstar.strategy.api.event.ModuleEventType;

public class ModuleStateMachineTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/****************/ 
	/** 开仓成功场景 **/
	/****************/
	//多开
	@Test
	public void testBuyOpen() {
		ModuleStateMachine state = new ModuleStateMachine("test", ModuleState.EMPTY);
		StateChangeListener listener = mock(StateChangeListener.class);
		state.addStateChangeListener(listener);
		state.transformForm(ModuleEventType.SIGNAL_CREATED);
		verify(listener).onChange(ModuleState.PLACING_ORDER);
		state.transformForm(ModuleEventType.ORDER_CONFIRMED);
		verify(listener).onChange(ModuleState.PENDING_ORDER);
		state.transformForm(ModuleEventType.BUY_TRADED);
		verify(listener).onChange(ModuleState.HOLDING_LONG);
	}
	
	//空开
	@Test
	public void testSellOpen() {
		ModuleStateMachine state = new ModuleStateMachine("test", ModuleState.EMPTY);
		StateChangeListener listener = mock(StateChangeListener.class);
		state.addStateChangeListener(listener);
		state.transformForm(ModuleEventType.SIGNAL_CREATED);
		verify(listener).onChange(ModuleState.PLACING_ORDER);
		state.transformForm(ModuleEventType.ORDER_CONFIRMED);
		verify(listener).onChange(ModuleState.PENDING_ORDER);
		state.transformForm(ModuleEventType.SELL_TRADED);
		verify(listener).onChange(ModuleState.HOLDING_SHORT);
	}
	
	/****************/ 
	/** 开仓失败场景 **/
	/****************/
	@Test
	public void testOpenFallback() {
		ModuleStateMachine state = new ModuleStateMachine("test", ModuleState.EMPTY);
		StateChangeListener listener = mock(StateChangeListener.class);
		state.addStateChangeListener(listener);
		state.transformForm(ModuleEventType.SIGNAL_CREATED);
		verify(listener).onChange(ModuleState.PLACING_ORDER);
		state.transformForm(ModuleEventType.ORDER_REQ_RETAINED);
		verify(listener).onChange(ModuleState.EMPTY);
		
		state.transformForm(ModuleEventType.SIGNAL_CREATED);
		verify(listener, times(2)).onChange(ModuleState.PLACING_ORDER);
		state.transformForm(ModuleEventType.ORDER_CONFIRMED);
		verify(listener).onChange(ModuleState.PENDING_ORDER);
		state.transformForm(ModuleEventType.REJECT_RISK_ALERTED);
		verify(listener).onChange(ModuleState.RETRIEVING_FOR_CANCAL);
		state.transformForm(ModuleEventType.ORDER_CANCELLED);
		verify(listener, times(2)).onChange(ModuleState.EMPTY);
		
		state.transformForm(ModuleEventType.SIGNAL_CREATED);
		verify(listener, times(3)).onChange(ModuleState.PLACING_ORDER);
		state.transformForm(ModuleEventType.ORDER_CONFIRMED);
		verify(listener, times(2)).onChange(ModuleState.PENDING_ORDER);
		state.transformForm(ModuleEventType.RETRY_RISK_ALERTED);
		verify(listener).onChange(ModuleState.RETRIEVING_FOR_RETRY);
		state.transformForm(ModuleEventType.ORDER_CANCELLED);
		verify(listener, times(4)).onChange(ModuleState.PLACING_ORDER);
		state.transformForm(ModuleEventType.ORDER_CONFIRMED);
		verify(listener, times(3)).onChange(ModuleState.PENDING_ORDER);
		state.transformForm(ModuleEventType.ORDER_CANCELLED);
		verify(listener, times(3)).onChange(ModuleState.EMPTY);
		
	}
	
	/****************/ 
	/** 平仓成功场景 **/
	/****************/
	//多平成功
	@Test
	public void testBuyClose() {
		ModuleStateMachine state = new ModuleStateMachine("test", ModuleState.HOLDING_SHORT);
		StateChangeListener listener = mock(StateChangeListener.class);
		state.addStateChangeListener(listener);
		state.transformForm(ModuleEventType.SIGNAL_CREATED);
		verify(listener).onChange(ModuleState.PLACING_ORDER);
		state.transformForm(ModuleEventType.ORDER_CONFIRMED);
		verify(listener).onChange(ModuleState.PENDING_ORDER);
		state.transformForm(ModuleEventType.BUY_TRADED);
		verify(listener).onChange(ModuleState.EMPTY);
	}
	
	//空平成功
	@Test
	public void testSellClose() {
		ModuleStateMachine state = new ModuleStateMachine("test", ModuleState.HOLDING_LONG);
		StateChangeListener listener = mock(StateChangeListener.class);
		state.addStateChangeListener(listener);
		state.transformForm(ModuleEventType.SIGNAL_CREATED);
		verify(listener).onChange(ModuleState.PLACING_ORDER);
		state.transformForm(ModuleEventType.ORDER_CONFIRMED);
		verify(listener).onChange(ModuleState.PENDING_ORDER);
		state.transformForm(ModuleEventType.SELL_TRADED);
		verify(listener).onChange(ModuleState.EMPTY);
	}
	
	/****************/ 
	/** 平仓失败场景 **/
	/****************/
	//多平失败
	@Test
	public void testBuyCloseFallback() {
		ModuleStateMachine state = new ModuleStateMachine("test", ModuleState.HOLDING_SHORT);
		StateChangeListener listener = mock(StateChangeListener.class);
		state.addStateChangeListener(listener);
		state.transformForm(ModuleEventType.SIGNAL_CREATED);
		verify(listener).onChange(ModuleState.PLACING_ORDER);
		state.transformForm(ModuleEventType.ORDER_CONFIRMED);
		verify(listener).onChange(ModuleState.PENDING_ORDER);
		state.transformForm(ModuleEventType.REJECT_RISK_ALERTED);
		verify(listener).onChange(ModuleState.RETRIEVING_FOR_CANCAL);
		state.transformForm(ModuleEventType.ORDER_CANCELLED);
		verify(listener).onChange(ModuleState.HOLDING_SHORT);
		
		state.transformForm(ModuleEventType.SIGNAL_CREATED);
		verify(listener, times(2)).onChange(ModuleState.PLACING_ORDER);
		state.transformForm(ModuleEventType.ORDER_CONFIRMED);
		verify(listener, times(2)).onChange(ModuleState.PENDING_ORDER);
		state.transformForm(ModuleEventType.RETRY_RISK_ALERTED);
		verify(listener).onChange(ModuleState.RETRIEVING_FOR_RETRY);
		state.transformForm(ModuleEventType.ORDER_CANCELLED);
		verify(listener, times(3)).onChange(ModuleState.PLACING_ORDER);
		state.transformForm(ModuleEventType.ORDER_CONFIRMED);
		verify(listener, times(3)).onChange(ModuleState.PENDING_ORDER);
		state.transformForm(ModuleEventType.ORDER_CANCELLED);
		verify(listener, times(2)).onChange(ModuleState.HOLDING_SHORT);
	}
	
	//空平失败
	@Test
	public void testSellCloseFallback() {
		ModuleStateMachine state = new ModuleStateMachine("test", ModuleState.HOLDING_LONG);
		StateChangeListener listener = mock(StateChangeListener.class);
		state.addStateChangeListener(listener);
		state.transformForm(ModuleEventType.SIGNAL_CREATED);
		verify(listener).onChange(ModuleState.PLACING_ORDER);
		state.transformForm(ModuleEventType.ORDER_CONFIRMED);
		verify(listener).onChange(ModuleState.PENDING_ORDER);
		state.transformForm(ModuleEventType.REJECT_RISK_ALERTED);
		verify(listener).onChange(ModuleState.RETRIEVING_FOR_CANCAL);
		state.transformForm(ModuleEventType.ORDER_CANCELLED);
		verify(listener).onChange(ModuleState.HOLDING_LONG);
		
		state.transformForm(ModuleEventType.SIGNAL_CREATED);
		verify(listener, times(2)).onChange(ModuleState.PLACING_ORDER);
		state.transformForm(ModuleEventType.ORDER_CONFIRMED);
		verify(listener, times(2)).onChange(ModuleState.PENDING_ORDER);
		state.transformForm(ModuleEventType.RETRY_RISK_ALERTED);
		verify(listener).onChange(ModuleState.RETRIEVING_FOR_RETRY);
		state.transformForm(ModuleEventType.ORDER_CANCELLED);
		verify(listener, times(3)).onChange(ModuleState.PLACING_ORDER);
		state.transformForm(ModuleEventType.ORDER_CONFIRMED);
		verify(listener, times(3)).onChange(ModuleState.PENDING_ORDER);
		state.transformForm(ModuleEventType.ORDER_CANCELLED);
		verify(listener, times(2)).onChange(ModuleState.HOLDING_LONG);
		
	}
	
	/***************/ 
	/**  止损场景  **/
	/***************/
	@Test
	public void testLongPositionStopLoss() {
		ModuleStateMachine state = new ModuleStateMachine("test", ModuleState.HOLDING_LONG);
		StateChangeListener listener = mock(StateChangeListener.class);
		state.addStateChangeListener(listener);
		state.transformForm(ModuleEventType.STOP_LOSS);
		verify(listener).onChange(ModuleState.PLACING_ORDER);
	}
	
	@Test
	public void testShortPositionStopLoss() {
		ModuleStateMachine state = new ModuleStateMachine("test", ModuleState.HOLDING_SHORT);
		StateChangeListener listener = mock(StateChangeListener.class);
		state.addStateChangeListener(listener);
		state.transformForm(ModuleEventType.STOP_LOSS);
		verify(listener).onChange(ModuleState.PLACING_ORDER);
	}
	
	/****************/
	/**	其他异常情况 **/
	/****************/
	@Test(expected = IllegalStateException.class)
	public void testCreateSignal() {
		ModuleStateMachine state = new ModuleStateMachine("test", ModuleState.PENDING_ORDER);
		state.transformForm(ModuleEventType.SIGNAL_CREATED);
	}
	
	@Test
	public void testOrderReqCreated() {
		ModuleStateMachine state = new ModuleStateMachine("test", ModuleState.PENDING_ORDER);
		state.transformForm(ModuleEventType.ORDER_CONFIRMED);
		state.transformForm(ModuleEventType.ORDER_REQ_CREATED);
		assertThat(state.getState()).isEqualTo(ModuleState.PENDING_ORDER);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testOrderReqRetained() {
		ModuleStateMachine state = new ModuleStateMachine("test", ModuleState.EMPTY);
		state.transformForm(ModuleEventType.ORDER_REQ_RETAINED);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testSellTraded() {
		ModuleStateMachine state = new ModuleStateMachine("test", ModuleState.EMPTY);
		state.transformForm(ModuleEventType.SELL_TRADED);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testBuyTraded() {
		ModuleStateMachine state = new ModuleStateMachine("test", ModuleState.EMPTY);
		state.transformForm(ModuleEventType.BUY_TRADED);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testOrderConfirmed() {
		ModuleStateMachine state = new ModuleStateMachine("test", ModuleState.EMPTY);
		state.transformForm(ModuleEventType.ORDER_CONFIRMED);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testRetryRiskAlerted() {
		ModuleStateMachine state = new ModuleStateMachine("test", ModuleState.EMPTY);
		state.transformForm(ModuleEventType.RETRY_RISK_ALERTED);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testRejectRiskAlerted() {
		ModuleStateMachine state = new ModuleStateMachine("test", ModuleState.EMPTY);
		state.transformForm(ModuleEventType.REJECT_RISK_ALERTED);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testOrderCancelled() {
		ModuleStateMachine state = new ModuleStateMachine("test", ModuleState.EMPTY);
		state.transformForm(ModuleEventType.ORDER_CANCELLED);
	}
}
