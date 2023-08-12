package application;

import io.zksync.wallet.ZkSyncWallet;
import methods.WalletService;
public class Main {
    public static final String EraLendAddress_USDC = "0xe62b571E9F40D158cB20796C56E93475d896c56D";

    public static void main(String[] args) {
//        BalanceService balanceService = new BalanceService();
//        balanceService.getBalancesOfAddress(EraLendAddress_USDC);
        WalletService service = new WalletService();
        ZkSyncWallet wallet = service.getWallet();
        try {
            System.out.println(wallet.getAddress());
            service.getBalanceOfEth();
//            service.unLockWallet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}