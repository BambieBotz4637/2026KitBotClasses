
package frc.robot.auto;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;

public class AutoDrive extends Command {

  private final DriveSubsystem driveSubsystem;
  private final Timer timer = new Timer();

  public AutoDrive(DriveSubsystem driveSubsystem) {
    this.driveSubsystem = driveSubsystem;
    addRequirements(driveSubsystem);
  }

  @Override
  public void initialize() {
    timer.reset();
    timer.start();
  }

  @Override
  public void execute() {
    // Drive straight forward
    driveSubsystem.arcadeDrive(0.5, 0.0);
  }

  @Override
  public void end(boolean interrupted) {
    driveSubsystem.arcadeDrive(0.0, 0.0);
    timer.stop();
  }

  @Override
  public boolean isFinished() {
    return timer.hasElapsed(3.0);
  }
}
