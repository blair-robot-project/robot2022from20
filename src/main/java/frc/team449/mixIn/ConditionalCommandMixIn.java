package frc.team449.mixIn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import java.util.function.BooleanSupplier;

/**
 * A mix-in for {@link edu.wpi.first.wpilibj2.command.Command} that annotates its constructor for
 * use with Jackson. Don't make sublasses of this.
 */
public abstract class ConditionalCommandMixIn {
  /**
   * @see ConditionalCommand#ConditionalCommand(edu.wpi.first.wpilibj2.command.Command,
   *     edu.wpi.first.wpilibj2.command.Command, java.util.function.BooleanSupplier)
   */
  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public ConditionalCommandMixIn(
      @JsonProperty(required = false) final Command onTrue,
      @JsonProperty(required = false) final Command onFalse,
      @JsonProperty(required = true) final BooleanSupplier condition) {}
}
