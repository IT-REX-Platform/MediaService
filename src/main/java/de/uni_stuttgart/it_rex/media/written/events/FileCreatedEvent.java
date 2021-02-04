package de.uni_stuttgart.it_rex.media.written.events;

import org.springframework.context.ApplicationEvent;

public final class FileCreatedEvent extends ApplicationEvent {
  /**
   * Video id.
   */
  private final Long id;

  /**
   * Constructor.
   *
   * @param source  the object that emitted the event.
   * @param videoId the video id.
   */
  public FileCreatedEvent(final Object source, final Long videoId) {
    super(source);
    this.id = videoId;
  }

  /**
   * Getter.
   *
   * @return the video id.
   */
  public Long getId() {
    return id;
  }
}
