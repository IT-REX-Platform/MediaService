package de.uni_stuttgart.it_rex.media.service.written.events;

import org.springframework.context.ApplicationEvent;

import java.util.UUID;

public final class FileCreatedEvent extends ApplicationEvent {
  /**
   * Video id.
   */
  private final UUID id;

  /**
   * Constructor.
   *
   * @param source  the object that emitted the event.
   * @param videoId the video id.
   */
  public FileCreatedEvent(final Object source, final UUID videoId) {
    super(source);
    this.id = videoId;
  }

  /**
   * Getter.
   *
   * @return the video id.
   */
  public UUID getId() {
    return id;
  }
}
