package frc.team449.oi.unidirectional.arcade;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import frc.team449.oi.unidirectional.OIUnidirectional;

/** An arcade-style dual joystick OI. */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    include = JsonTypeInfo.As.WRAPPER_OBJECT,
    property = "@class")
public abstract class OIArcade implements OIUnidirectional {

  /** Whether or not to scale the left and right outputs so the max output is 1. */
  private final boolean rescaleOutputs;
  /** Cached forwards and rotational output. */
  @Nullable private double[] fwdRotOutputCached;
  /** Cached left-right output values */
  @Nullable private double[] leftRightOutputCached;

  /**
   * Default constructor.
   *
   * @param rescaleOutputs Whether or not to scale the left and right outputs so the max output is
   *     1. Defaults to false.
   */
  @JsonCreator
  public OIArcade(final boolean rescaleOutputs) {
    this.rescaleOutputs = rescaleOutputs;
  }

  /**
   * Whether the driver is trying to drive straight.
   *
   * @return True if the driver is trying to drive straight, false otherwise.
   */
  @Override
  @Log
  public boolean commandingStraight() {
    return getFwdRotOutputCached()[1] == 0;
  }

  /**
   * The output to be given to the left and right sides of the drive.
   *
   * @return An array of length 2, where the 1st element is the output for the left and the second
   *     for the right, both from [-1, 1].
   */
  @Override
  @NotNull
  public double[] getLeftRightOutput() {
    fwdRotOutputCached = getFwdRotOutput();

    // Unscaled, unclipped values for left and right output.
    final double tmpLeft = fwdRotOutputCached[0] + fwdRotOutputCached[1];
    final double tmpRight = fwdRotOutputCached[0] - fwdRotOutputCached[1];

    // If left is too large
    if (Math.abs(tmpLeft) > 1) {
      if (rescaleOutputs) {
        // Rescale right, return left clipped to [-1, 1]
        return new double[] {Math.signum(tmpLeft), tmpRight / Math.abs(tmpLeft)};
      } else {
        // Return left clipped to [-1, 1], don't change right
        return new double[] {Math.signum(tmpLeft), tmpRight};
      }
    } else if (Math.abs(tmpRight) > 1) { // If right is too large
      if (rescaleOutputs) {
        // Rescale left, return right clipped to [-1, 1]
        return new double[] {tmpLeft / Math.abs(tmpRight), Math.signum(tmpRight)};
      } else {
        // Return right clipped to [-1, 1], don't change left
        return new double[] {tmpLeft, Math.signum(tmpRight)};
      }
    } else {
      // Return unaltered if nothing is too large
      return new double[] {tmpLeft, tmpRight};
    }
  }

  /**
   * The cached output to be given to the left and right sides of the drive.
   *
   * @return An array of length 2, where the 1st element is the output for the left and the second
   *     for the right, both from [-1, 1].
   */
  @Override
  @NotNull
  public double[] getLeftRightOutputCached() {
    return leftRightOutputCached != null
        ? leftRightOutputCached
        : (leftRightOutputCached = getLeftRightOutput());
  }

  /**
   * The cached forwards and rotational movement given to the drive.
   *
   * @return An array of length 2, where the first element is the forwards output and the second is
   *     the rotational, both from [-1, 1]
   */
  @Override
  @NotNull
  public double[] getFwdRotOutputCached() {
    return fwdRotOutputCached != null
        ? fwdRotOutputCached
        : (fwdRotOutputCached = getFwdRotOutput());
  }

  /** Updates all cached values with current ones. */
  @Override
  public void update() {
    fwdRotOutputCached = getFwdRotOutput();
    leftRightOutputCached = getLeftRightOutput();
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
  //                "left",
  //                "right",
  //                "commandingStraight",
  //                "fwd",
  //                "rot"
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
  //                getLeftRightOutputCached()[0],
  //                getLeftRightOutputCached()[1],
  //                commandingStraight(),
  //                getFwdRotOutputCached()[0],
  //                getFwdRotOutputCached()[1]
  //        };
  //    }
}
