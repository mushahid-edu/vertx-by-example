import io.vertx.core.Future
import io.vertx.core.json.JsonObject
import io.vertx.groovy.core.eventbus.Message
import io.vertx.lang.groovy.GroovyVerticle

class EventVerticle extends GroovyVerticle {

    @Override
    void start(Future startFuture) {
        vertx.eventBus().consumer('event.verticle', this.&doSomething)

        if ((Math.round(Math.random()*1))==1) {            // Randomly succeed or fail deployment of EventVerticle
            startFuture.complete()
        } else {
            startFuture.fail('Random deployment failure of EventVerticle')
        }
    }

    void doSomething(Message<JsonObject> msg) {
        if ((Math.round(Math.random()*1))==1) {
            msg.reply(msg.body())
        } else {
            msg.fail(1, 'Random Failure')
	}
    }
}
