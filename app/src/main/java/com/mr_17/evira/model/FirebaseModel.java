package com.mr_17.evira.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                        node_profilePic = "profilePic",
                        node_profileImages = "Profile Images",
                        node_joiningDate = "joiningDate";

    public static DatabaseReference databaseRef_root = FirebaseDatabase.getInstance().getReference();
    public static DatabaseReference databaseRef_users = databaseRef_root.child(node_users);

    //public static StorageReference storageRef_root = FirebaseStorage.getInstance().getReference();
    //public static StorageReference storageRef_profileImages = storageRef_root.child(node_profileImages);

}
