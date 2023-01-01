package com.mr_17.evira.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseModel
{
    public static String node_users = "Users",
                        node_uid = "uid",
                        node_firstName = "firstName",
                        node_lastName = "lastName",
                        node_username = "username",
                        node_email = "email",
                        node_phone = "phoneNumber",
                        node_pan = "panNumber",
                        node_aadhar = "aadharNumber",
                        node_address = "address",
                        node_aadharPic = "aadharPic",
                        node_panPic = "panPic",
                        node_profilePic = "profilePic",
                        node_profileImages = "Profile Images",
                        node_aadharImages = "Aadhar Images",
                        node_panImages = "Pan Images",
                        node_joiningDate = "joiningDate",
                        node_uids = "UIDs",
                        node_isBlocked = "isBlocked",
                        node_isAdmin = "isAdmin",
                        node_isSuperAdmin = "isSuperAdmin";

    public static DatabaseReference databaseRef_root = FirebaseDatabase.getInstance().getReference();
    public static DatabaseReference databaseRef_users = databaseRef_root.child(node_users);
    public static DatabaseReference databaseRef_uids = databaseRef_root.child(node_uids);

    public static StorageReference storageRef_root = FirebaseStorage.getInstance().getReference();
    public static StorageReference storageRef_profileImages = storageRef_root.child(node_profileImages);
    public static StorageReference storageRef_aadharImages = storageRef_root.child(node_aadharImages);
    public static StorageReference storageRef_panImages = storageRef_root.child(node_panImages);
}
