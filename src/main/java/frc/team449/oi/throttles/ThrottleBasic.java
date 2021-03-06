package frc.team449.oi.throttles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.Joystick;
import frc.team449.jacksonWrappers.MappedJoystick;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;

/** A class representing a single axis on a joystick. */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class ThrottleBasic implements Throttle {

  /** The stick we're using */
  @NotNull protected final Joystick stick;

  /** The axis on the joystick we care about. */
  private final int axis;

  /** Whether or not the controls should be inverted */
  private final boolean inverted;

  /** The cached value of the output. */
  protected double cachedOutput;

  /**
   * Default constructor.
   *
   * @param stick The Joystick object being used
   * @param axis The axis being used. 0 is X, 1 is Y, 2 is Z.
   * @param inverted Whether or not to invert the joystick input. Defaults to false.
   */
  @JsonCreator
  public ThrottleBasic(
      @NotNull @JsonProperty(required = true) final MappedJoystick stick,
      @JsonProperty(required = true) final int axis,
      final boolean inverted) {
    this.stick = stick;
    this.axis = axis;
    this.inverted = inverted;
  }

  /**
   * Gets the raw value from the stick and inverts it if necessary.
   *
   * @return The raw joystick output, on [-1, 1].
   */
  @Log
  @Override
  public double getValue() {
    return (inverted ? -1 : 1) * stick.getRawAxis(axis);
  }

  /**
   * Get the cached output of the throttle this object represents.
   *
   * @return The output from [-1, 1].
   */
  @Override
  @Log
  public double getValueCached() {
    return cachedOutput;
  }

  /** Updates all cached values with current ones. */
  @Override
  public void update() {
    cachedOutput = getValue();
  }

  /**
   * Get the result to use in PIDController.
   *
   * @return the result to use in PIDController
   */
  @Log
  public double pidGet() {
    return (inverted ? -1 : 1) * stick.getRawAxis(axis);
  }

  @Override
  public String configureLogName() {
    return "Throttle " + stick.getName() + " axis " + axis;
  }

  //    /**
  //     * Get the headers for the data this subsystem logs every loop.
  //     *
  //     * @return An N-length array of String labels for data, where N is the length of the
  // Object[] returned by getData().
  //     */
  //    @NotNull
  //    @Override
  //    public String[] getHeader() {
  //        return new String[]{
  //                "value"
  //        };
  //    }
  //
  //    /**
  //     * Get the data this subsystem logs every loop.
  //     *
  //     * @return An N-length array of Objects, where N is the number of labels given by getHeader.
  //     */
  //    @NotNull
  //    @Override
  //    public Object[] getData() {
  //        return new Object[]{
  //                getValueCached()
  //        };
  //    }
  //
  //    /**
  //     * Get the name of this object.
  //     *
  //     * @return A string that will identify this object in the log file.
  //     */
  //    @NotNull
  //    @Override
  //    public String getLogName() {
  //        return "Stick_" + stick.getPort() + "_Axis_" + axis;
  //    }
}
