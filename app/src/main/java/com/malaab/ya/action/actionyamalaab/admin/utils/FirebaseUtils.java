package com.malaab.ya.action.actionyamalaab.admin.utils;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.admin.annotations.NotificationType;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Notification;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.User;


public class FirebaseUtils {

    public static DatabaseReference getUsersRef() {
        return FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_TABLE);
    }

    public static DatabaseReference getNotificationsRef() {
        return FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_NOTIFICATIONS_TABLE);
    }


    public static void sendNotificationToUser(@NotificationType final String type,
                                              final String fromUserUid, final String fromUsername, final String fromUserProfileImage,
                                              final String toUserUid, final String toUsername, final String toUserProfileImage) {
        getUsersRef()
                .child(toUserUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot != null && dataSnapshot.exists()) {

                            User toUser = dataSnapshot.getValue(User.class);
                            if (toUser != null) {
                                if (toUser.isActive) {
                                    String uid = getNotificationsRef().push().getKey();

                                    Notification notification = new Notification();
                                    notification.uid = uid;
                                    notification.type = type;
                                    notification.title = "";
                                    notification.message = "";
                                    notification.fromUserUid = StringUtils.isEmpty(fromUserUid) ? "" : fromUserUid;
                                    notification.fromUsername = StringUtils.isEmpty(fromUsername) ? "" : fromUsername;
                                    notification.fromUserProfileImage = StringUtils.isEmpty(fromUserProfileImage) ? "" : fromUserProfileImage;
                                    notification.toUserUid = toUser.uId;
                                    notification.toUsername = toUser.getUserFullName();
                                    notification.toUserProfileImage = StringUtils.isEmpty(toUser.profileImageUrl) ? "" : toUser.profileImageUrl;
                                    notification.toFCMToken = toUser.fcmToken;

                                    sendNotification(notification);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        AppLogger.d("sendBookingNotificationToAdmin onCancelled = " + databaseError.getMessage());
                    }
                });
    }

    public static void sendNotificationToUser(@NotificationType final String type,
                                              final String toUserId,
                                              final String fromUserUid, final String fromUsername, final String fromUserProfileImage) {

        sendNotificationToUser(type, toUserId, fromUserUid, fromUsername, fromUserProfileImage, "");
    }

    public static void sendNotificationToUser(@NotificationType final String type,
                                              final String toUserId,
                                              final String fromUserUid, final String fromUsername, final String fromUserProfileImage,
                                              final String message) {
        getUsersRef().child(toUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot != null && dataSnapshot.exists()) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user != null) {
                                if (user.isActive) {
                                    String uid = getNotificationsRef().push().getKey();

                                    Notification notification = new Notification();
                                    notification.uid = uid;
                                    notification.type = type;
                                    notification.title = "";
                                    notification.message = message;
                                    notification.fromUserUid = StringUtils.isEmpty(fromUserUid) ? "" : fromUserUid;
                                    notification.fromUsername = StringUtils.isEmpty(fromUsername) ? "" : fromUsername;
                                    notification.fromUserProfileImage = StringUtils.isEmpty(fromUserProfileImage) ? "" : fromUserProfileImage;
                                    notification.toUserUid = user.uId;
                                    notification.toUsername = user.getUserFullName();
                                    notification.toUserProfileImage = StringUtils.isEmpty(user.profileImageUrl) ? "" : user.profileImageUrl;
                                    notification.toFCMToken = user.fcmToken;

                                    sendNotification(notification);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        AppLogger.d("sendNotificationToUser onCancelled = " + databaseError.getMessage());
                    }
                });
    }


    public static void sendNotificationToOwner(@NotificationType final String type, final String ownerId,
                                               final String fromUserUid, final String fromUsername, final String fromUserProfileImage) {

        getUsersRef().child(ownerId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot != null && dataSnapshot.exists()) {
                            User owner = dataSnapshot.getValue(User.class);
                            if (owner != null) {
                                if (owner.isActive) {
                                    String uid = getNotificationsRef().push().getKey();

                                    Notification notification = new Notification();
                                    notification.uid = uid;
                                    notification.type = type;
                                    notification.title = "";
                                    notification.message = "";
                                    notification.fromUserUid = StringUtils.isEmpty(fromUserUid) ? "" : fromUserUid;
                                    notification.fromUsername = StringUtils.isEmpty(fromUsername) ? "" : fromUsername;
                                    notification.fromUserProfileImage = StringUtils.isEmpty(fromUserProfileImage) ? "" : fromUserProfileImage;
                                    notification.toUserUid = owner.uId;
                                    notification.toUsername = owner.getUserFullName();
                                    notification.toUserProfileImage = StringUtils.isEmpty(owner.profileImageUrl) ? "" : owner.profileImageUrl;
                                    notification.toFCMToken = owner.fcmToken;

                                    sendNotification(notification);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        AppLogger.d("sendNotificationToOwner onCancelled = " + databaseError.getMessage());
                    }
                });
    }


    public static void sendNotification(final Notification notification) {
        getNotificationsRef()
                .child(notification.uid)
                .setValue(notification)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AppLogger.w("sendNotification to " + notification.toUsername + " -> onSuccess");
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AppLogger.w("sendNotification to " + notification.toUsername + " -> onComplete");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AppLogger.w("sendNotification to " + notification.toUsername + " -> Error " + e.getLocalizedMessage());
                    }
                });
    }

    //    public static DatabaseReference getUserRef(String email) {
//        return FirebaseDatabase.getInstance()
//                .getReference(Constants.USERS_KEYS)
//                .child(email);
//    }
//
//    public static DatabaseReference getPostRef() {
//        return FirebaseDatabase.getInstance()
//                .getReference(Constants.POSTS_KEY);
//    }
//
//    public static Query getPostQuery() {
//        return getPostRef().orderByChild(Constants.TIME_CREATED_KEY);
//    }
//
//    public static DatabaseReference getPostLikedRef() {
//        return FirebaseDatabase.getInstance()
//                .getReference(Constants.POSTS_LIKED_KEY);
//    }
//
//    public static DatabaseReference getPostLikedRef(String postId) {
//        return getPostLikedRef().child(getCurrentUser().getEmail()
//                .replace(".", ","))
//                .child(postId);
//    }
//
//    public static FirebaseUser getCurrentUser() {
//        return FirebaseAuth.getInstance().getCurrentUser();
//    }
//
//    public static String getUid() {
//        String path = FirebaseDatabase.getInstance().getReference().push().toString();
//        return path.substring(path.lastIndexOf("/") + 1);
//    }
//
//    public static StorageReference getImagesSRef() {
//        return FirebaseStorage.getInstance().getReference(Constants.POST_IMAGES);
//    }
//
//    public static DatabaseReference getMyPostRef() {
//        return FirebaseDatabase.getInstance().getReference(Constants.MY_POSTS)
//                .child(getCurrentUser().getEmail().replace(".", ","));
//    }
//
//    public static DatabaseReference getCommentRef(String postId) {
//        return FirebaseDatabase.getInstance().getReference(Constants.COMMENTS_KEY)
//                .child(postId);
//    }
//
//    public static DatabaseReference getMyRecordRef() {
//        return FirebaseDatabase.getInstance().getReference(Constants.USER_RECORD)
//                .child(getCurrentUser().getEmail().replace(".", ","));
//    }
//
//    public static void addToMyRecord(String node, final String id) {
//        FirebaseUtils.getMyRecordRef().child(node).runTransaction(new Transaction.Handler() {
//            @Override
//            public Transaction.Result doTransaction(MutableData mutableData) {
//                ArrayList<String> myPostCollection;
//                if (mutableData.getValue() == null) {
//                    myPostCollection = new ArrayList<>(1);
//                    myPostCollection.add(id);
//                    mutableData.setValue(myPostCollection);
//                } else {
//                    myPostCollection = (ArrayList<String>) mutableData.getValue();
//                    myPostCollection.add(id);
//                    mutableData.setValue(myPostCollection);
//                }
//                return Transaction.success(mutableData);
//            }
//
//            @Override
//            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//
//            }
//        });
//    }
//
//    //I'll be creating the dynamic link now. Since all of our post has a unique Id in them we
//    // want that in our url. so let's start
//
//    public static String generateDeepLink(String uid) {
//        //We first need to get our firebase deep link prefix
//        //The https://firebasetutorial.com/<uid> is what will be recieved in our app when this
//        // linked is clicked. We need to add our package name so that android knows which app to
//        // open
//
//        return "https://t53y3.app.goo.gl/?link=https://firebasetutorial.com/" + uid +
//                "&apn=com.getmore.getmoreapp.firebasepostandchattutorial";
//    }
//
//    public static DatabaseReference getSharedRef(String postId) {
//        return getPostRef().child(postId).child(Constants.NUM_SHARES_KEY);
//    }
//
//    public static DatabaseReference getNotificationRef() {
//        return FirebaseDatabase.getInstance().getReference(Constants.NOTIFICATION_KEY).push();
//    }

}