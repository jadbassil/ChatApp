var stompClient = null;
var user = null;

function setConnected(connected){
	if(connected){
		$("#msg-history").show()
	}else{
		$("#msg-history").hide();
	}
	$("#msg-history").html("");
}


function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function connect(userId, chatId){
	console.log(userId + " "+ chatId);
	disconnect();
    var socket = new SockJS('/ChatApp-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/group/'+chatId, function (message) {
            showMessage(userId, JSON.parse(message.body));
        });
    });
}


function sendMessage(userId, chatId) {
	var time = new Date();
    stompClient.send("/app/"+chatId, {}, JSON.stringify({'message': $("#btn-input").val(), 'senderId': userId, 'time': time, 'chatId': chatId}));
}

function showMessage(userId, message) {
	console.log(message.senderId+'  '+userId);
	if(message.senderId != userId)
		$("#msg-history").append(    
			"<div class='incoming_msg'>" +
		      "<!-- <div class='incoming_msg_img'> <img src='https://ptetutorials.com/images/user-profile.png' alt='sunil'> </div> -->" +
		      "<div class='received_msg'>" +
		       " <div class='received_withd_msg'>" +
 		          "<p>"+message.message+"</p>" +
		          "<span class='time_date'>"+message.time+"</div>" +
		      "</div>" +
		    "</div>"
	    );
	else{
		if(message.senderId == userId)
			$("#msg-history").append(    
				"<div class='outgoing_msg'>" +
			      "<div class='sent_msg'>" +
	 		          "<p>"+message.message+"</p>" +
			          "<span class='time_date'>"+message.time+"</span>" +
			      "</div>" +
			    "</div>"
		    );
	}
}