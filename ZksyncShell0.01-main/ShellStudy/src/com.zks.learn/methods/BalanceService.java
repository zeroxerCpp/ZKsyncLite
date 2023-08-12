package methods;

import cn.hutool.core.util.ObjectUtil;
import constant.Constant;
import constant.Tokens;

import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

public class BalanceService {
    HashMap<String,String> res = new HashMap<>();

    /**
     * 获取代币列表
     */
//    public HashMap<String,String> getTokenList(){
//        try {
//            if (res.size()==0){
//                ZksTokens response = zksync.zksGetConfirmedTokens(0, (short) 30).send();
//                response.getResult().forEach(token -> {
//                    res.put(token.getL2Address(),token.getSymbol());
//                });
//            }
//            return res;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    public void getBalancesOfAddress(String address){
//        if (res.size()==0){
//            this.getTokenList();
//        }
//        ZksAccountBalances balances = null;
//        try {
//            balances = zksync.zksGetAllAccountBalances(address).send();
//            balances.getBalances().forEach((key,value)->{
//                String symbol = res.get(key);
//                if (ObjectUtil.isNotEmpty(symbol))
//                {
//                    String balance = "";
//                    if (symbol.equals(Tokens.ETH)){
//                        balance = Convert.fromWei(new BigDecimal(value), Convert.Unit.ETHER).toPlainString();
//                    }else{
//                        BigInteger[] integers = value.divideAndRemainder(BigInteger.valueOf(1000 * 1000));
//                        balance = integers[0]+"."+integers[1];
//                    }
//                    System.out.println(symbol+":"+balance);
//
//                }
//            });
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//


}
