// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

'use-strict'
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


exports.sendMessageNotification = functions.database.ref('/users/chat/{user_id}/{recipient_id}/messages/thread/notifications/{notification_id}').onWrite((snapshot, context) => {

    const recipient_id = context.params.recipient_id;
    const user_id = context.params.user_id;
    const notification_id = context.params.notification_id;

    const device_token = admin.database().ref(`/users/profile/${recipient_id}/deviceToken/device_token`).once('value');
    const public_name = admin.database().ref(`/users/profile/${recipient_id}/publicName`).once('value');

    return Promise.all([device_token, public_name]).then(result => {

        const token = result[0].val();
        const publicName = result[1].val();

        const payload = {
            notification: {
                title : "New Message Received",
                body : `${publicName} has sent you a Message`,
                icon : "default",
                click_action : "com.example.datebook.TARGETNOTIFICATION"
            },
            data : {
                recipient_id : recipient_id
            }
        }
        return admin.messaging().sendToDevice(token, payload);
    })
})