package home.serg.billsplitter.model;

public class BillTransaction {
    String creditor;
    String debtor;
    int debt;
    
    public BillTransaction(String creditor, String debtor, int debt) {
        this.creditor = creditor;
        this.debtor = debtor;
        this.debt = debt;
    }
    
    public String getCreditor() {
        return creditor;
    }
    
    public String getDebtor() {
        return debtor;
    }
    
    public int getDebt() {
        return debt;
    }
}
