package frc.team449.drive.shifting.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import org.jetbrains.annotations.NotNull;
import frc.team449.drive.shifting.DriveShiftable;

/**
 * Override or unoverride whether we're autoshifting. Used to stay in low gear for pushing matches
 * and more!
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class OverrideAutoShift extends InstantCommand {

  /** Whether or not to override. */
  private final boolean override;

  /** The drive subsystem to execute this command on. */
  @NotNull private final DriveShiftable subsystem;

  /**
   * Default constructor
   *
   * @param drive The drive subsystem to execute this command on.
   * @param override Whether or not to override autoshifting.
   */
  @JsonCreator
  public OverrideAutoShift(
      @NotNull @JsonProperty(required = true) DriveShiftable drive,
      @JsonProperty(required = true) boolean override) {
    subsystem = drive;
    this.override = override;
  }

  /** Log on initialization */
  @Override
  public void initialize() {
    Shuffleboard.addEventMarker(
        "OverrideAutoShift init", this.getClass().getSimpleName(), EventImportance.kNormal);
    // Logger.addEvent("OverrideAutoShift init", this.getClass());
  }

  /** Override autoshifting. */
  @Override
  public void execute() {
    // Set whether or not we're overriding
    subsystem.setOverrideAutoshift(override);
  }

  /** Log when this command ends. */
  @Override
  public void end(boolean interrupted) {
    if (interrupted) {
      Shuffleboard.addEventMarker(
          "OverrideAutoShift Interrupted!",
          this.getClass().getSimpleName(),
          EventImportance.kNormal);
    }
    Shuffleboard.addEventMarker(
        "OverrideAutoShift end", this.getClass().getSimpleName(), EventImportance.kNormal);
    // Logger.addEvent("OverrideAutoShift end", this.getClass());
  }
}
