$(document).ready(function () {

    var USER_ID = "user" + Math.round(Math.random() * 1000);
    var SERVER_URL = "ws://" + location.host + "/api/v1/chat/" + USER_ID;

    var $message = $("#message");
    var $history = $("#history");

    var socket = new WebSocket(SERVER_URL);

    socket.onopen = function () {
        console.log("Connected");
    };

    socket.onclose = function (event) {
        if (event.wasClean) {
            console.log("Connection closed");
        } else {
            console.log("Connection error");
        }
        console.log("Error code: " + event.code + ", reason: " + event.reason);
    };

    var addMessage = function (user, message) {
        $("<li></li>").html(
            $("<strong></strong>").text(user + ": ")
                .add($("<span></span>").text(message))
        ).appendTo($history);
    };

    socket.onmessage = function (event) {
        console.log("Received message: " + event.data);
        var message = JSON.parse(event.data);
        addMessage(message.user, message.message);
    };

    socket.onerror = function (error) {
        console.log("Error " + error.message);
    };

    $("#user-name").text(USER_ID);

    $("#chat-form-submit").click(function (event) {
        var message = $message.val();
        socket.send(message);
        console.log("Sending message: " + message);
        $message.val("");
        addMessage(USER_ID, message);
        event.preventDefault();
        return false;
    });


});
