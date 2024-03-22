import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;

public class SecureGringottsDeposit {
    public void depositToGringotts(String item, double value) {
        try {
            //Define file path
            Path filePath = Paths.get("gringotts_ledger_secure.txt");

            //If not exist create new
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            //Define ACL file Permission
            UserPrincipal owner = Files.getOwner(filePath);
            AclEntry.Builder builder = AclEntry.newBuilder();
            builder.setPermissions(AclEntryPermission.READ_DATA, AclEntryPermission.WRITE_DATA);
            builder.setPrincipal(owner);
            builder.setType(AclEntryType.ALLOW);
            builder.setFlags(AclEntryFlag.FILE_INHERIT, AclEntryFlag.DIRECTORY_INHERIT);
            AclEntry aclEntry = builder.build();

            //Get ACL or Create New
            AclFileAttributeView aclView = Files.getFileAttributeView(filePath, AclFileAttributeView.class);
            List<AclEntry> acl = aclView.getAcl();
            acl.add(aclEntry);

            //Set File ACL
            aclView.setAcl(acl);

            //Write to file
            String data = "Deposited: " + item + ", Value: " + value + "\n";
            Files.write(filePath, data.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Deposit Cloak
    public static void main(String[] args) {
        SecureGringottsDeposit deposit = new SecureGringottsDeposit();
        deposit.depositToGringotts("Invisibility Cloak", 500.0);
    }
}
