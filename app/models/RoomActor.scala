package models

import akka.actor.{Actor, ActorRef}
import play.api.Logger
import play.api.libs.json.JsValue

import collection.mutable.ListBuffer

class RoomAction

case class JoinInRoom(name: String, user: ActorRef) extends RoomAction

case class LeaveRoom(name: String, user: ActorRef) extends RoomAction

case class MessageRoom(sender: ActorRef, message: JsValue) extends RoomAction

class RoomActor extends Actor {

  private var members = ListBuffer.empty[ActorRef]

  override def receive: Receive = {

    case JoinInRoom(name, user) =>
      members += user
      Logger.info(s"User $name joined")

    case LeaveRoom(name, user) =>
      members -= user
      Logger.info(s"User $name exited")

    case MessageRoom(sender, message) =>
      members
        .filter(_ != sender)
        .foreach(_ ! MessageRoom(sender, message))

      Logger.info(s"Sending message: $message")
  }

}