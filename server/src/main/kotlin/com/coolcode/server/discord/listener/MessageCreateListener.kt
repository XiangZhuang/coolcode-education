package com.coolcode.server.discord.listener

import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.`object`.entity.channel.PrivateChannel
import discord4j.core.util.EntityUtil
import discord4j.discordjson.json.DMCreateRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

//@Component
final class MessageCreateListener(
    private val discordClient: GatewayDiscordClient,
): EventListener<MessageCreateEvent>() {
    final override fun getEventType() = MessageCreateEvent::class
    final override fun dmOnly() = true

    override fun execute(event: MessageCreateEvent): Mono<Void> {
        return discordClient.restClient.userService
            .createDM(
                DMCreateRequest
                    .builder()
                    .recipientId(event.message.author.get().id.asString())
                    .build()
            ).map {
                EntityUtil.getChannel(discordClient, it)
            }.cast(PrivateChannel::class.java).flatMap {
                it.createMessage("Hey I'm a very busy hacker. Please use the commands to communicate with me.").block()
                Mono.empty()
            }
    }
}