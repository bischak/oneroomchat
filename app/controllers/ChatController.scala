package controllers

import akka.actor.{Props, _}
import models._
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

object ChatController {

  class UserChannelActor(user: String, out: ActorRef) extends Actor {

    private val room = Akka.system.actorSelection("/user/chat_room")

    override def preStart() = room ! JoinInRoom(user, self)

    override def postStop() = room ! LeaveRoom(user, self)

    def receive = {

      case channelMessage: String =>
        val jsonMessage = Json.obj("user" -> user, "message" -> channelMessage)
        room ! MessageRoom(self, jsonMessage)

      case MessageRoom(sender, message) =>
        out ! message

    }
  }

  def chat(user: String) = WebSocket.acceptWithActor[String, JsValue] { request =>
    out => Props(new UserChannelActor(user, out))
  }

}
