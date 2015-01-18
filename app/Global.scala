import akka.actor.Props
import models.RoomActor
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.{Application, GlobalSettings}

object Global extends GlobalSettings {

  override def onStart(app: Application): Unit = {
    Akka.system.actorOf(Props[RoomActor], "chat_room")
  }
}
