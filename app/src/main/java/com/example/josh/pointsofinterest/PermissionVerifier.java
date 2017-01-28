package com.example.josh.pointsofinterest;

import android.content.pm.PackageManager;

public class PermissionVerifier {

    private final String[] mPermissions;
    private final int [] mGrantResults;

    public PermissionVerifier(String[] permissions, int[] grantResults) {
        mPermissions = permissions;
        mGrantResults = grantResults;
    }

    public boolean isGranted(String permissionName) {
        for (int i = 0; i < mPermissions.length; i++) {
            if (mPermissions[i].equals(permissionName)) {
                return mGrantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }
}
