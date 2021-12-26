package frc.team449;

import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.PerpetualCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.team449.mixIn.CommandBaseMixIn;
import frc.team449.mixIn.CommandGroupMixIn;
import frc.team449.mixIn.ConditionalCommandMixIn;
import frc.team449.mixIn.DoubleSolenoidMixIn;
import frc.team449.mixIn.PerpetualCommandMixIn;
import frc.team449.mixIn.PrintCommandMixIn;
import frc.team449.mixIn.SubsystemMixIn;
import frc.team449.mixIn.UseCLASSIncludeWRAPPER_OBJECTMixIn;
import frc.team449.mixIn.WaitCommandMixIn;
import frc.team449.mixIn.WaitUntilCommandMixIn;

/**
 * A Jackson {@link com.fasterxml.jackson.databind.Module} for adding mix-in annotations to classes.
 */
public class WPIModule extends SimpleModule {

  /** Default constructor */
  public WPIModule() {
    super("WPIModule");
  }

  /**
   * Mixes in some mix-ins to the given context.
   *
   * @param context the context to set up
   */
  @Override
  public void setupModule(final SetupContext context) {
    super.setupModule(context);

    context.setMixInAnnotations(Subsystem.class, SubsystemMixIn.class);

    context.setMixInAnnotations(DoubleSolenoid.class, DoubleSolenoidMixIn.class);

    context.setMixInAnnotations(Command.class, UseCLASSIncludeWRAPPER_OBJECTMixIn.class);
    context.setMixInAnnotations(CommandBase.class, CommandBaseMixIn.class);

    // TODO Verify how this actually works (probably it's because Jackson ignores the constructor's
    //   name and only looks at the signature).
    context.setMixInAnnotations(SequentialCommandGroup.class, CommandGroupMixIn.class);
    context.setMixInAnnotations(ParallelCommandGroup.class, CommandGroupMixIn.class);

    context.setMixInAnnotations(WaitCommand.class, WaitCommandMixIn.class);
    context.setMixInAnnotations(WaitUntilCommand.class, WaitUntilCommandMixIn.class);
    context.setMixInAnnotations(PrintCommand.class, PrintCommandMixIn.class);
    context.setMixInAnnotations(ConditionalCommand.class, ConditionalCommandMixIn.class);
    context.setMixInAnnotations(PerpetualCommand.class, PerpetualCommandMixIn.class);

    context.setMixInAnnotations(Button.class, UseCLASSIncludeWRAPPER_OBJECTMixIn.class);
  }
}
