// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OIConstants;
import frc.robot.auto.ExampleAuto;
import frc.robot.commands.Eject;
import frc.robot.commands.LaunchSequence;
import frc.robot.subsystems.CANFuelSubsystem;
import frc.robot.subsystems.DriveSubsystem;


/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems
  private final DriveSubsystem driveSubsystem = new DriveSubsystem();
  private final CANFuelSubsystem fuelSubsystem = new CANFuelSubsystem();

  // The driver's controller
   GenericHID m_driverController = new  GenericHID(OIConstants.kDriverControllerPort);
   
  // The operator's controller
  private final CommandXboxController m_operatorController = new CommandXboxController(1);
   

  // The autonomous chooser
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    configureBindings();

 
    // set up the shuffleboard tab
        Shuffleboard.getTab("Autonomous")
                .add("Auto Selector", m_chooser)
                .withWidget(BuiltInWidgets.kComboBoxChooser)
                .withPosition(0, 0)
                .withSize(2, 1);

        SmartDashboard.putData("Auto choices", m_chooser);
        m_chooser.setDefaultOption("Do Nothing", "bleh");
        // m_chooser.addOption("Sample Auto", "Sample Auto");
        m_chooser.addOption("BlueMidAuto", "BlueMidAuto");
        m_chooser.addOption("Sample Auto", "Sample Auto");

        SmartDashboard.putData("Auto choices", m_chooser);

        /*
        NamedCommands.registerCommand("Drive Forward", new AutoDrive(drive, 0.5, 0.0));
        NamedCommands.registerCommand("Spin in Place", new AutoDrive(drive, 0.0, 0.5));
        NamedCommands.registerCommand("Drive Backwards", new AutoDrive(drive, -0.5, 0.0));
        NamedCommands.registerCommand("Drive Forward and Spin", new AutoDrive(drive, 0.5, 0.5));
        NamedCommands.registerCommand("Drive Backwards and Spin", new AutoDrive(drive, -0.5, 0.5));
        NamedCommands.registerCommand("Sample Auto", new ExampleAuto(driveSubsystem, fuelSubsystem));
        NamedCommands.registerCommand("BlueMidAuto", new BlueMidAuto(driveSubsystem, fuelSubsystem));
        */

    // Set the options to show up in the Dashboard for selecting auto modes. If you
    // add additional auto modes you can add additional lines here with
    // autoChooser.addOption
   // autoChooser.setDefaultOption("Autonomous", new ExampleAuto(driveSubsystem, fuelSubsystem));

  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be
   * created via the {@link Trigger#Trigger(java.util.function.BooleanSupplier)}
   * constructor with an arbitrary predicate, or via the named factories in
   * {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses
   * for {@link CommandXboxController Xbox}/
   * {@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
   * controllers or
   * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {

    new JoystickButton(m_driverController, 6)
        .whileTrue(new RunCommand(
            () -> driveSubsystem.setX(),
            driveSubsystem));

    new JoystickButton(m_driverController, 8)
        .onTrue(new InstantCommand(
            () -> driveSubsystem.zeroHeading(),
            driveSubsystem));

  // While the left bumper on operator controller is held, intake Fuel
  m_operatorController.leftBumper().whileTrue(new RunCommand(
        () -> fuelSubsystem.setIntakeLauncherRoller(0.5), fuelSubsystem));
             // While the right bumper on the operator controller is held, spin up for 1
  // second, then launch fuel. When the button is released, stop.
  m_operatorController.rightBumper().whileTrue(new LaunchSequence(fuelSubsystem));
  // While the A button is held on the operator controller, eject fuel back out
  // the intake
  m_operatorController.a().whileTrue(new Eject(fuelSubsystem));

  // Set the default command for the drive subsystem to the command provided by
  // factory with the values provided by the joystick axes on the driver
  // controller. The Y axis of the controller is inverted so that pushing the
  // stick away from you (a negative value) drives the robot forwards (a positive
  // value)

driveSubsystem.setDefaultCommand(
    new RunCommand(
        () -> driveSubsystem.drive(
            -m_driverController.getRawAxis(1),
            -m_driverController.getRawAxis(0),
            -m_driverController.getRawAxis(4),
            true),
            driveSubsystem));
  
   // fuelSubsystem.setDefaultCommand(fuelSubsystem.run(() -> fuelSubsystem.stop()));          
  }
   /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   * */
   
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
   // return m_chooser.getSelected();
    String selectedAuto = m_chooser.getSelected();

    switch (selectedAuto) {
      case "BlueMidAuto":
      case "Sample Auto":
        // Return the ExampleAuto (replace with specific autos if available)
        return new ExampleAuto(driveSubsystem, fuelSubsystem);
      case "bleh":
      default:
        // Do nothing
        return new InstantCommand();
      }
    }
}