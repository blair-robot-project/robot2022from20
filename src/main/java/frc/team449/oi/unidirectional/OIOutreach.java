package frc.team449.oi.unidirectional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.button.Button;
import io.github.oblarg.oblog.annotations.Log;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class OIOutreach implements OIUnidirectional {

  /** The OI with higher priority that overrides if it has any input. */
  @NotNull private final OIUnidirectional overridingOI;

  /** The OI with lower priority that gets overriden. */
  @NotNull private final OIUnidirectional overridenOI;

  /** A button that overrides the lower priority controller */
  @NotNull private final Button button;

  /** The cached outputs for the left and right sides of the drive. */
  private double[] cachedLeftRightOutput;

  /** The cached forwards and rotational outputs. */
  private double[] cachedFwdRotOutput;

  /** The data to log. Field to avoid garbage collection. */
  private Object[] loggingData, overridenData, overridingData;

  @JsonCreator
  public OIOutreach(
      @NotNull @JsonProperty(required = true) final OIUnidirectional overridingOI,
      @NotNull @JsonProperty(required = true) final OIUnidirectional overridenOI,
      @NotNull @JsonProperty(required = true) final Button button) {
    this.overridingOI = overridingOI;
    this.overridenOI = overridenOI;
    this.button = button;
  }

  /**
   * The output to be given to the left and right sides of the drive.
   *
   * @return An array of length 2, where the 1st element is the output for the left and the second
   *     for the right, both from [-1, 1].
   */
  @Override
  public double[] getLeftRightOutput() {
    if (!Arrays.equals(this.overridingOI.getLeftRightOutput(), new double[] {0, 0})
        || this.button.get()) {
      return this.overridingOI.getLeftRightOutput();
    } else {
      return this.overridenOI.getLeftRightOutput();
    }
  }

  /**
   * The cached output to be given to the left and right sides of the drive.
   *
   * @return An array of length 2, where the 1st element is the output for the left and the second
   *     for the right, both from [-1, 1].
   */
  @Override
  @Log
  public double[] getLeftRightOutputCached() {
    return this.cachedLeftRightOutput;
  }

  /**
   * The forwards and rotational movement given to the drive.
   *
   * @return An array of length 2, where the first element is the forwards output and the second is
   *     the rotational, both from [-1, 1]
   */
  @Override
  public double[] getFwdRotOutput() {
    if (!Arrays.equals(this.overridingOI.getLeftRightOutput(), new double[] {0, 0})
        || this.button.get()) {
      return this.overridingOI.getFwdRotOutput();
    } else {
      return this.overridenOI.getFwdRotOutput();
    }
  }

  /**
   * The cached forwards and rotational movement given to the drive.
   *
   * @return An array of length 2, where the first element is the forwards output and the second is
   *     the rotational, both from [-1, 1]
   */
  @Override
  @Log
  public double[] getFwdRotOutputCached() {
    return this.cachedFwdRotOutput;
  }

  /**
   * Whether the driver is trying to drive straight.
   *
   * @return True if the driver is trying to drive straight, false otherwise.
   */
  @Override
  @Log
  public boolean commandingStraight() {
    return this.getLeftRightOutputCached()[0] == this.getLeftRightOutputCached()[1];
  }

  /** Updates all cached values with current ones. */
  @Override
  public void update() {
    this.overridenOI.update();
    this.overridingOI.update();
    this.cachedLeftRightOutput = this.getLeftRightOutput();
    this.cachedFwdRotOutput = this.getFwdRotOutput();
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
  //        String[] toRet = new String[overridenOI.getHeader().length +
  // overridingOI.getHeader().length];
  //        for (int i = 0; i < overridenOI.getHeader().length; i++) {
  //            toRet[i] = "overriden." + overridenOI.getHeader()[i];
  //        }
  //
  //        for (int i = 0; i < overridingOI.getHeader().length; i++) {
  //            toRet[i + overridenOI.getHeader().length] = "overriding." +
  // overridingOI.getHeader()[i];
  //        }
  //        return toRet;
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
  //        overridenData = overridenOI.getData();
  //        overridingData = overridingOI.getData();
  //        //Concatenate the arrays, overriden then overriding
  //        loggingData = Arrays.copyOf(overridenData, overridenData.length +
  // overridingData.length);
  //        System.arraycopy(overridingData, 0, loggingData, overridenData.length,
  // overridingData.length);
  //        return loggingData;
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
  //        return "OIOutreach";
  //    }
}
