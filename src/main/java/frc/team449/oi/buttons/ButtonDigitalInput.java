package frc.team449.oi.buttons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.team449.jacksonWrappers.MappedDigitalInput;
import org.jetbrains.annotations.NotNull;

/** A button triggered off of a digital input switch on the RoboRIO. */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class ButtonDigitalInput extends Button {

  /** The input to read from. */
  @NotNull private final DigitalInput input;

  /**
   * Default constructor.
   *
   * @param input The input to read from.
   */
  @JsonCreator
  public ButtonDigitalInput(
      @NotNull @JsonProperty(required = true) final MappedDigitalInput input) {
    this.input = input;
  }

  /**
   * Get whether this button is pressed
   *
   * @return true if the all the ports in the MappedDigitalInput are true, false otherwise.
   */
  @Override
  public boolean get() {
    return this.input.get();
  }
}
