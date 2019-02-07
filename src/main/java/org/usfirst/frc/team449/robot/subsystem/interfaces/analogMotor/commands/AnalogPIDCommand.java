package org.usfirst.frc.team449.robot.subsystem.interfaces.analogMotor.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj.command.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.other.Clock;
import org.usfirst.frc.team449.robot.other.Logger;
import org.usfirst.frc.team449.robot.subsystem.interfaces.analogMotor.SubsystemAnalogMotor;

import java.util.function.DoubleSupplier;
import org.usfirst.frc.team449.robot.other.BufferTimer;

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class AnalogPIDCommand<T extends Subsystem & SubsystemAnalogMotor> extends PIDCommand {

    @NotNull private DoubleSupplier processVariableSupplier;

    /**
     * Supplies the setpoint
     */
    @NotNull private DoubleSupplier setpointSupplier;

    /**
     * Determines whether input should be inverted
     */
    private boolean inverted;

    /**
     * The time this command was initiated
     */
    protected long startTime;

    /**
     * How long this command is allowed to run for (in milliseconds)
     */
    private final long timeout;

    /**
     * The drive subsystem to execute this command on and to get the gyro reading from.
     */
    @NotNull private T subsystem;

    /**
     * The range in which output is turned off to prevent "dancing" around the setpoint.
     */
    private final double deadband;

     BufferTimer onTargetBuffer;

    /**
     *
     * @param onTargetBuffer A buffer timer for having the loop be on target before it stops running. Can be null for
     *      *                          no buffer.
     * @param absoluteTolerance The maximum number of degrees off from the target at which we can be considered within
     *                          tolerance.
     * @param subsystem The drive subsystem to execute this command on and to get the gyro reading from.
     * @param processVariableSupplier Make the processVariableSupplier equal the setpoint
     * @param deadband The range in which output is turned off to prevent "dancing" around the setpoint.
     * @param kP Proportional gain. Defaults to zero.
     * @param kI Integral gain. Defaults to zero.
     * @param kD Derivative gain. Defaults to zero.
     * @param setpoint The setpoint, in degrees from 180 to -180.
     * @param setpointSupplier Supplies the setpoint
     * @param inverted Determines whether input should be inverted
     * @param timeout How long this command is allowed to run for (in milliseconds)
     */
    @JsonCreator
    public AnalogPIDCommand(@JsonProperty(required = true) double absoluteTolerance,
                            @JsonProperty(required = true) T subsystem,
                            @NotNull @JsonProperty(required = true) DoubleSupplier processVariableSupplier,
                            double deadband,
                            double kP,
                            double kI,
                            double kD,
                            double setpoint,
                            @Nullable DoubleSupplier setpointSupplier,
                            boolean inverted,
                            @Nullable BufferTimer onTargetBuffer,
                            long timeout) {
        super(kP, kI, kD);
        requires(subsystem);

        //The drive subsystem to execute this command on and to get the gyro reading from.
        this.subsystem = subsystem;

        //Checks in input should be inverted
        this.inverted = inverted;

        //Convert from seconds to milliseconds
        this.timeout = (long) (timeout * 1000);

        //This is how long we have to be within the tolerance band. Multiply by loop period for time in ms.
        this.onTargetBuffer = onTargetBuffer;

        //Set the absolute tolerance to be considered on target within.
        this.getPIDController().setAbsoluteTolerance(absoluteTolerance);

        //Supplying a setpoint, in degrees from 180 to -180.
        if (setpointSupplier == null) {
            this.setpointSupplier = () -> setpoint;
        } else {
            this.setpointSupplier = setpointSupplier;
        }

        // Make the processVariableSupplier equal the setpoint
        this.processVariableSupplier = processVariableSupplier;


        //Set a deadband around the setpoint, in appropriate units, within which don't move, to avoid "dancing"
        this.deadband = deadband;

        //Setpoint is always zero since we subtract it off ourselves, from the input.
        this.setSetpoint(0);
    }

    /**
     * Whether or not the loop is on target. Use this instead of {@link edu.wpi.first.wpilibj.PIDController}'s
     * onTarget.
     *
     * @return True if on target, false otherwise.
     */
    protected boolean onTarget() {
        if (onTargetBuffer == null) {
            return this.getPIDController().onTarget();
        } else {
            return onTargetBuffer.get(this.getPIDController().onTarget());
        }
    }

    /**
     * Deadband the output of the PID loop.
     *
     * @param output The output from the WPILib PID loop.
     * @return That output after being deadbanded with the map-given deadband.
     */
    protected double deadbandOutput(double output) {
        return Math.abs(this.getPIDController().getError()) > deadband ? output : 0;
    }

    /**
     * Returns the input for the pid loop.
     *
     * <p>It returns the input for the pid loop, so if this command was based off of a gyro, then it
     * should return the angle of the gyro.
     *
     * <p>All subclasses of {@link PIDCommand} must override this method.
     *
     * <p>This method will be called in a different thread then the {@link Scheduler} thread.
     *
     * @return the value the pid loop should use as input
     */
    @Override
    protected double returnPIDInput() {
        double value = processVariableSupplier.getAsDouble();
        if(Double.isNaN(value)){
            return 0;
        }
        return !inverted ? value - setpointSupplier.getAsDouble() : -value - setpointSupplier.getAsDouble();
    }

    /**
     * Uses the value that the pid loop calculated. The calculated value is the "output" parameter.
     * This method is a good time to set motor values, maybe something along the lines of
     * <code>driveline.tankDrive(output, -output)</code>
     *
     * <p>All subclasses of {@link PIDCommand} must override this method.
     *
     * <p>This method will be called in a different thread then the {@link Scheduler} thread.
     *
     * @param output the value the pid loop calculated
     */
    @Override
    protected void usePIDOutput(double output) {
        output = deadbandOutput(output);
        subsystem.set(output);
    }

    /**
     * Clip a degree number to the NavX's -180 to 180 system.
     *
     * @param theta The angle to clip, in degrees.
     * @return The equivalent of that number, clipped to be between -180 and 180.
     */
    @Contract(pure = true)
    protected static double clipTo180(double theta) {
        return (theta + 180) % 360 - 180;
    }

    /**
     * Set up the start time and setpoint.
     */
    @Override
    protected void initialize() {
        Logger.addEvent("NavXTurnToAngle init.", this.getClass());
        //Set up start time
        this.startTime = Clock.currentTimeMillis();
        this.setSetpoint(clipTo180(setpointSupplier.getAsDouble()));
        //Make sure to enable the controller!
        this.getPIDController().enable();
    }

    /**
     * Exit when the robot reaches the setpoint or enough time has passed.
     *
     * @return True if timeout seconds have passed or the robot is on target, false otherwise.
     */
    @Override
    protected boolean isFinished() {
        //The PIDController onTarget() is crap and sometimes never returns true because of floating point errors, so
        // we need a timeout
        return onTarget() || Clock.currentTimeMillis() - startTime > timeout;
    }

}