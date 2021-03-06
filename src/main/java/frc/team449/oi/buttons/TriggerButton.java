package frc.team449.oi.buttons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.team449.oi.throttles.Throttle;
import org.jetbrains.annotations.NotNull;

/**
 * A button that gets triggered by a specific throttle being held down at or over a certain amount.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class TriggerButton extends Button {

  /** The relevant throttle. */
  @NotNull private final Throttle throttle;

  /** The percentage pressed to trigger at, from (0, 1] */
  private final double triggerAt;

  /**
   * Argument-based constructor.
   *
   * @param throttle The relevant throttle.
   * @param triggerAt The percentage pressed to trigger at, from (0, 1]
   */
  @JsonCreator
  public TriggerButton(
      @NotNull @JsonProperty(required = true) final Throttle throttle,
      @JsonProperty(required = true) final double triggerAt) {
    this.throttle = throttle;
    this.triggerAt = triggerAt;
  }

  /**
   * Get whether this button is pressed.
   *
   * @return true if the throttle's output is greater than or equal to the trigger threshold, false
   *     otherwise.
   */
  @Override
  public boolean get() {
    return Math.abs(this.throttle.getValueCached()) >= this.triggerAt;
  }
}
