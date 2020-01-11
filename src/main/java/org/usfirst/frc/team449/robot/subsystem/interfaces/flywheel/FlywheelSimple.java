package org.usfirst.frc.team449.robot.subsystem.interfaces.flywheel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.usfirst.frc.team449.robot.generalInterfaces.simpleMotor.SimpleMotor;

/**
 * A simple flywheel subsystem.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class)
public class FlywheelSimple extends SubsystemBase implements SubsystemFlywheel, Loggable {

    /**
     * The motor that controls the flywheel.
     */
    @NotNull
    private final SimpleMotor shooterMotor;

    /**
     * The motor that controls the feeder.
     */
    @NotNull
    private final SimpleMotor feederMotor;

    /**
     * The velocity for the flywheel to run at, on [-1, 1].
     */
    private final double shooterVelocity;

    /**
     * The velocity for the feeder to run at, on [-1, 1].
     */
    private final double feederVelocity;

    /**
     * Time from giving the flywheel voltage to being ready to fire, in seconds.
     */
    private final double spinUpTime;

    /**
     * The current state of the flywheel.
     */
    @NotNull
    private SubsystemFlywheel.FlywheelState state;

    /**
     * Default constructor
     *
     * @param shooterMotor    The motor that controls the flywheel.
     * @param feederMotor     The motor that controls the feeder.
     * @param shooterVelocity The velocity for the flywheel to run at, on [-1, 1].
     * @param feederVelocity  The velocity for the feeder to run at, on [-1, 1]. Defaults to 1.
     * @param spinUpTime      Time from giving the flywheel voltage to being ready to fire, in seconds. Defaults to 0.
     */
    @JsonCreator
    public FlywheelSimple(@NotNull @JsonProperty(required = true) SimpleMotor shooterMotor,
                          @NotNull @JsonProperty(required = true) SimpleMotor feederMotor,
                          @JsonProperty(required = true) double shooterVelocity,
                          @Nullable Double feederVelocity,
                          double spinUpTime) {
        this.shooterMotor = shooterMotor;
        this.feederMotor = feederMotor;
        this.shooterVelocity = shooterVelocity;
        this.feederVelocity = feederVelocity != null ? feederVelocity : 1;
        this.spinUpTime = spinUpTime;
        this.state = FlywheelState.OFF;
    }

    /**
     * Turn the flywheel on to a map-specified speed.
     */
    @Override
    public void turnFlywheelOn() {
        shooterMotor.enable();
        shooterMotor.setVelocity(shooterVelocity);
    }

    /**
     * Turn the flywheel off.
     */
    @Override
    public void turnFlywheelOff() {
        shooterMotor.setVelocity(0);
        shooterMotor.disable();
    }

    /**
     * Start feeding balls into the flywheel.
     */
    @Override
    public void turnFeederOn() {
        feederMotor.enable();
        feederMotor.setVelocity(feederVelocity);
    }

    /**
     * Stop feeding balls into the flywheel.
     */
    @Override
    public void turnFeederOff() {
        feederMotor.setVelocity(0);
        feederMotor.disable();
    }

    /**
     * @return The current state of the flywheel.
     */
    @NotNull
    @Override
    @Log
    public SubsystemFlywheel.FlywheelState getFlywheelState() {
        return state;
    }

    /**
     * @param state The state to switch the flywheel to.
     */
    @Override
    public void setFlywheelState(@NotNull SubsystemFlywheel.FlywheelState state) {
        this.state = state;
    }

    /**
     * @return Time from giving the flywheel voltage to being ready to fire, in milliseconds.
     */
    @Override
    @Log
    public double getSpinUpTimeoutSecs() {
        return spinUpTime;
    }
}
