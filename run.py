import shutil
import subprocess
import sys


def main():
    if len(sys.argv) != 2:
        print(f"Usage: {sys.argv[0]} <exercise_number>")
        sys.exit(1)

    exercise = sys.argv[1]

    # Copy skeleton POM
    shutil.copyfile("pom_skeleton.xml", "pom.xml")

    # Replace placeholder
    with open("pom.xml", "r", encoding="utf-8") as f:
        content = f.read()

    content = content.replace("NUMBER", exercise)

    with open("pom.xml", "w", encoding="utf-8") as f:
        f.write(content)

    # Compile project
    print("Building project...")
    with open("build.log", "w", encoding="utf-8") as log:
        result = subprocess.run(
            ["mvn", "clean", "install"],
            stdout=log,
            stderr=subprocess.STDOUT
        )

    if result.returncode != 0:
        print("Build failed. See build.log for details.")
        sys.exit(result.returncode)

    # Run Java program
    print("Running application...")
    result = subprocess.run(["java", "-jar", "target/lab-09.jar"])
    sys.exit(result.returncode)


if __name__ == "__main__":
    main()