package frc.team449.drive.unidirectional.commands.motionprofiling;

import com.fasterxml.jackson.annotation.*;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.math.trajectory.constraint.TrajectoryConstraint;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.team449.drive.unidirectional.DriveUnidirectionalWithGyro;
import frc.team449.jacksonWrappers.MappedPIDController;
import io.github.oblarg.oblog.Loggable;

import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    include = JsonTypeInfo.As.WRAPPER_OBJECT,
    property = "@class")
public class RamseteControllerGoToPosition extends CommandBase implements Loggable {

  private final DriveUnidirectionalWithGyro drivetrain;
  private final MappedPIDController leftPidController;
  private final MappedPIDController rightPidController;
  private final Pose2d endingPose;
  private final List<Translation2d> translations;
  private RamseteCommand wrappedCommand;
  private final TrajectoryConfig config;

  @JsonCreator
  public RamseteControllerGoToPosition(
      @JsonProperty(required = true) DriveUnidirectionalWithGyro drivetrain,
      @JsonProperty(required = true) final double maxSpeedMeters,
      @JsonProperty(required = true) final double maxAccelMeters,
      Double maxCentripetalAcceleration,
      @JsonProperty(required = true) MappedPIDController leftPidController,
      @JsonProperty(required = true) MappedPIDController rightPidController,
      @JsonProperty(required = true) Pose2d endingPose,
      List<Translation2d> translations,
      boolean reversed) {
    this.drivetrain = drivetrain;
    this.leftPidController = leftPidController;
    this.rightPidController = rightPidController;
    this.endingPose = endingPose;
    this.translations = translations;

    TrajectoryConstraint voltageConstraint =
        new DifferentialDriveVoltageConstraint(
            drivetrain.getLeftFeedforwardCalculator(), drivetrain.getDriveKinematics(), 12);

    // Create config for trajectory
    config =
        new TrajectoryConfig(maxSpeedMeters, maxAccelMeters)
            .setKinematics(drivetrain.getDriveKinematics())
            .addConstraint(voltageConstraint)
            .setReversed(reversed);

    addRequirements(drivetrain);
  }

  @Override
  public void initialize() {
    wrappedCommand =
        new RamseteCommand(
            TrajectoryGenerator.generateTrajectory(
                drivetrain.getCurrentPose(),
                translations == null ? new ArrayList<>() : translations,
                endingPose,
                config),
            drivetrain::getCurrentPose,
            new RamseteController(),
            drivetrain.getLeftFeedforwardCalculator(),
            drivetrain.getDriveKinematics(),
            drivetrain::getWheelSpeeds,
            leftPidController,
            rightPidController,
            drivetrain::setVoltage);
    wrappedCommand.initialize();
  }

  @Override
  public void execute() {
    wrappedCommand.execute();
  }

  @Override
  public void end(boolean interrupted) {
    wrappedCommand.end(interrupted);
  }

  @Override
  public boolean isFinished() {
    return wrappedCommand.isFinished();
  }
}
