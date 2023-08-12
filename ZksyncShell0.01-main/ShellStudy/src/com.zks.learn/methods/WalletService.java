package methods;

import cn.hutool.core.util.ObjectUtil;
import constant.Constant;
import constant.SecretInfo;

import io.zksync.domain.ChainId;
import io.zksync.domain.TimeRange;
import io.zksync.domain.fee.TransactionFee;
import io.zksync.domain.fee.TransactionFeeDetails;
import io.zksync.domain.fee.TransactionFeeRequest;
import io.zksync.domain.fee.TransactionType;
import io.zksync.domain.state.AccountState;
import io.zksync.domain.token.Token;
import io.zksync.provider.Provider;
import io.zksync.signer.DefaultEthSigner;
import io.zksync.signer.EthSigner;
import io.zksync.signer.ZkSigner;
import io.zksync.wallet.ZkSyncWallet;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

//lite
public class WalletService {
    public ZkSyncWallet wallet;
    Web3j web3j = Web3j.build(new HttpService(Constant.ZksMainNet));
    EthSigner ethSigner = DefaultEthSigner.fromRawPrivateKey(web3j, SecretInfo.privateKey);
    ZkSigner zkSigner = ZkSigner.fromEthSigner(ethSigner, ChainId.Mainnet);

    public ZkSyncWallet getWallet(){
        if (ObjectUtil.isEmpty(wallet)){
            wallet  =  ZkSyncWallet.build(ethSigner, zkSigner, Provider.defaultProvider(ChainId.Mainnet));
        }
        return wallet;
    }
/**
 * 解锁 zkSync 帐户
 * 要在 zkSync 网络中进行任何交易，您必须将 ZkSigner 的公钥注册到 EthSigner 提供的帐户中。
 */
    public void unLockWallet() throws Exception {
        AccountState state = wallet.getState();
        // Check if account exists in zkSync network
        if (ObjectUtil.isEmpty(state.getId())){
            throw new Exception("Unknown account");
        }
        // This should be done once thus we need to check if account pubkey is different or unset
        if (!wallet.isSigningKeySet()){
        // We should get fee amount required for execution the transaction
            TransactionFeeRequest feeRequest = TransactionFeeRequest.builder()
                    .address(state.getAddress())
                    .transactionType(TransactionType.CHANGE_PUB_KEY_ONCHAIN)
                    .tokenIdentifier(Token.createETH().getAddress())
                    .build();
            String balanceStr = state.getCommitted().getBalances().getOrDefault("ETH", "0");
            BigDecimal balance = Convert.fromWei(balanceStr, Convert.Unit.ETHER);
            System.out.println("Eth balance:"+balance);
            if (balance.compareTo(BigDecimal.valueOf(0.001))>0){
            TransactionFeeDetails fee = wallet.getProvider().getTransactionFee(feeRequest);
            System.out.println("gas fee"+fee.getGasFee());
            System.out.println("gas price wei:"+fee.getGasPriceWei());
            System.out.println("Total gas fee:"+fee.getGasFee());
        // Send transaction for setting your public key hash
            String hash = wallet.setSigningKey(
                    TransactionFee.builder()
                            .fee(fee.getTotalFeeInteger())
                            .feeToken(Token.createETH().getAddress())
                            .build(),
                    state.getCommitted().getNonce(),
                    false,
                    new TimeRange()
            );
            System.out.println(hash);
            }else {
                System.out.println("余额小于0.001");
            }

        }
    }

    public String transfer(String to){
        AccountState state = wallet.getState();

        TransactionFeeRequest feeRequest = TransactionFeeRequest.builder()
                .address(ethSigner.getAddress())
                .transactionType(TransactionType.TRANSFER)
                .tokenIdentifier(Token.createETH().getAddress())
                .build();
        TransactionFeeDetails fee = wallet.getProvider().getTransactionFee(feeRequest);
        String hash = wallet.syncTransfer(
                to,
                Convert.toWei("0.1", Convert.Unit.ETHER).toBigInteger(),
                TransactionFee.builder()
                        .fee(fee.getTotalFeeInteger())
                        .feeToken(Token.createETH().getAddress())
                        .build(),
                state.getCommitted().getNonce(),
                new TimeRange()
        );
        System.out.println(hash);
        return hash;
    }

    public void getBalanceOfEth(){
        AccountState state = wallet.getState();
//        String balanceStr = state.getCommitted().getBalances().getOrDefault("ETH", "0");
        state.getCommitted().getBalances().forEach((key,value)->{
            System.out.println(key+":"+value);
        });
//        BigDecimal balance = Convert.fromWei(balanceStr, Convert.Unit.ETHER);
//        System.out.println("Eth balance:"+balance);
    }





}
