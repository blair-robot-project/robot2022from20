package frc.team449.drive.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import org.jetbrains.annotations.NotNull;
import frc.team449.drive.DriveSubsystem;

/** Resets the positions of the motors of the given drive subsystem. */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class ResetPosition extends InstantCommand {

  /** The subsystem to execute this command on. */
  @NotNull private final DriveSubsystem subsystem;

  /**
   * Default constructor
   *
   * @param subsystem The subsystem to execute this command on.
   */
  @JsonCreator
  public ResetPosition(@NotNull @JsonProperty(required = true) DriveSubsystem subsystem) {
    this.subsystem = subsystem;
  }

  /** Log when this command is initialized */
  @Override
  public void initialize() {
    Shuffleboard.addEventMarker(
        "EnableMotors init.", this.getClass().getSimpleName(), EventImportance.kNormal);
    // Logger.addEvent("EnableMotors init.", this.getClass());
  }

  /** Do the state change. */
  @Override
  public void execute() {
    subsystem.resetPosition();
  }

  /** Log when this command ends */
  @Override
  public void end(boolean interrupted) {
    if (interrupted) {
      Shuffleboard.addEventMarker(
          "EnableMotors Interrupted!", this.getClass().getSimpleName(), EventImportance.kNormal);
    }
    Shuffleboard.addEventMarker(
        "EnableMotors end.", this.getClass().getSimpleName(), EventImportance.kNormal);
  }
}
