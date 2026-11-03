package com.example.data.model

object EducationalProjectsData {
    val projects: List<EducationalProject> = listOf(
        EducationalProject(
            id = "proj_ohms_law",
            title = "Verification of Ohm's Law & Circuit Resistance Determination",
            subject = "Physics / Science Practical",
            grade = "Class 10 & 12 (Board Core Practical)",
            durationText = "45 mins Practical Lab",
            practicalCategory = "Physics Lab",
            objective = "To verify Ohm's Law by finding the relationship between potential difference (V) and current (I) across a conductor and determine its electrical resistance per centimeter.",
            materialsRequired = listOf(
                "Nichrome resistance wire (100 cm)",
                "DC Voltmeter (0-3 V range)",
                "DC Ammeter (0-3 A range)",
                "Battery eliminator / 2 dry cells (3V - 4V)",
                "Rheostat (variable resistor 0-100 Ω)",
                "One-way plug key",
                "Copper connecting wires & Sandpaper"
            ),
            theoryAndPrinciple = "Ohm's Law states that at constant temperature, current flowing through a conductor is directly proportional to the potential difference across its terminals:\n\nV ∝ I  =>  V = I × R\n\nWhere R is resistance. A graph of V (y-axis) vs I (x-axis) yields a straight line passing through the origin. The slope of this line equals the resistance R = ΔV / ΔI.",
            stepByStepProcedure = listOf(
                "1. Clean ends of connecting wires using sandpaper to eliminate insulating oxide coating.",
                "2. Assemble circuit in series: Battery (+) -> Ammeter (+) -> Resistance Wire -> Rheostat -> Key -> Battery (-).",
                "3. Connect the Voltmeter strictly in PARALLEL across the ends of the resistance wire.",
                "4. Check zero error on both Voltmeter and Ammeter before inserting the key.",
                "5. Insert key and slide rheostat to record initial readings of current I and voltage V.",
                "6. Shift rheostat contact to 5 distinct positions, noting pairs of (V, I) each time.",
                "7. Calculate R = V / I for every observation and plot V vs I on graph paper."
            ),
            observationTable = listOf(
                "Trial 1 (Low Current)" to "V = 0.40 V, I = 0.20 A => R = 2.00 Ω",
                "Trial 2 (Medium Low)" to "V = 0.80 V, I = 0.40 A => R = 2.00 Ω",
                "Trial 3 (Medium High)" to "V = 1.20 V, I = 0.60 A => R = 2.00 Ω",
                "Trial 4 (High Current)" to "V = 1.60 V, I = 0.80 A => R = 2.00 Ω",
                "Trial 5 (Max Current)" to "V = 2.00 V, I = 1.00 A => R = 2.00 Ω"
            ),
            calculations = "Mean Resistance R_avg = (2.0 + 2.0 + 2.0 + 2.0 + 2.0) / 5 = 2.00 Ω.\nLength of wire L = 100 cm.\nResistance per cm = R / L = 2.00 / 100 = 0.02 Ω/cm.\nGraph Slope ΔV / ΔI = (2.00 - 0.40) / (1.00 - 0.20) = 1.60 / 0.80 = 2.00 Ω.",
            vivaQuestions = listOf(
                "Why is an Ammeter always connected in series?" to "An ammeter has negligibly low internal resistance, so inserting it in series allows full circuit current to pass without altering circuit impedance.",
                "Why is a Voltmeter connected in parallel?" to "A voltmeter has very high internal resistance, so when placed across components, it draws negligible current and accurately measures potential drop.",
                "Does Ohm's Law hold for semiconductors or diodes?" to "No, Ohm's law only holds for ohmic conductors (metals) at constant temperature. Diodes and transistors are non-ohmic."
            ),
            boardSubmissionFormat = "CBSE Practical Format: Aim -> Apparatus -> Principle -> Circuit Diagram -> Observation Table -> Graph Plot -> Result -> Precautions.",
            isCompleted = true
        ),
        EducationalProject(
            id = "proj_ph_indicators",
            title = "Acid-Base Detection & Natural Turmeric Indicators Experiment",
            subject = "Chemistry Practical",
            grade = "Class 9 & 10 (Syllabus Lab Work)",
            durationText = "30 mins Experiment",
            practicalCategory = "Chemistry Practical",
            objective = "To test acidic, basic, and neutral properties of common household solutions using natural indicator (Turmeric paste) and universal pH test strips.",
            materialsRequired = listOf(
                "Ground Turmeric powder & alcohol/water",
                "Filter paper strips (soaked in turmeric and dried)",
                "Red & Blue Litmus paper",
                "Universal pH indicator strip with color chart",
                "Lemon juice (Citric acid)",
                "Vinegar (Acetic acid)",
                "Baking soda solution (Sodium bicarbonate)",
                "Detergent / Soap solution (Basic)",
                "Distilled water",
                "Test tubes and dropper"
            ),
            theoryAndPrinciple = "Indicators change color depending on hydronium ion concentration [H+].\nTurmeric contains 'curcumin', a natural polyphenol that remains bright yellow in acidic and neutral environments (pH < 7.4) but turns deep reddish-brown in basic solutions (pH > 8.5).\npH Scale: 0 to 6 (Acidic), 7 (Neutral), 8 to 14 (Basic).",
            stepByStepProcedure = listOf(
                "1. Prepare fresh Turmeric test paper by brushing turmeric paste on white filter paper and letting it dry in shade.",
                "2. Cut into 1 cm x 5 cm test strips.",
                "3. Take 5 clean test tubes labeled: Lemon Juice, Vinegar, Water, Baking Soda, Soap Solution.",
                "4. Place one drop of each sample onto a turmeric paper strip.",
                "5. Observe immediate color change and record in practical table.",
                "6. Test same solutions with universal pH paper to find numerical pH value."
            ),
            observationTable = listOf(
                "Lemon Juice" to "Turmeric: Yellow (No change) | Litmus: Blue to Red | pH = 2.2 (Strong Acid)",
                "Vinegar" to "Turmeric: Yellow (No change) | Litmus: Blue to Red | pH = 3.0 (Weak Acid)",
                "Distilled Water" to "Turmeric: Yellow | Litmus: No change | pH = 7.0 (Neutral)",
                "Baking Soda Sol." to "Turmeric: Reddish Brown | Litmus: Red to Blue | pH = 8.5 (Mild Base)",
                "Soap / Detergent" to "Turmeric: Deep Crimson Red | Litmus: Red to Blue | pH = 10.0 (Strong Base)"
            ),
            calculations = "pH = -log10[H+]. As base is added to turmeric, curcumin loses protons to form a resonant quinoid structure reflecting red wavelengths.",
            vivaQuestions = listOf(
                "Why does a curry stain on a white shirt turn red when washed with soap?" to "Soap contains alkaline basic salts (sodium carboxylates), turning the turmeric's curcumin red.",
                "What happens when lemon juice is squeezed over the red soap stain?" to "The citric acid neutralizes the alkaline soap, restoring curcumin to its yellow acidic form!"
            ),
            boardSubmissionFormat = "Lab Journal Format: Aim -> Principle -> Materials -> Reaction Mechanism -> Color Table -> Conclusions.",
            isCompleted = false
        ),
        EducationalProject(
            id = "proj_math_clinometer",
            title = "Constructing a Clinometer & Trigonometric Height Estimation",
            subject = "Mathematics Project Work",
            grade = "Class 10 (Heights and Distances)",
            durationText = "1 hour Hands-on STEM",
            practicalCategory = "Math Model",
            objective = "Build a handheld clinometer to measure angle of elevation and calculate height of an inaccessible structure (school building, tree, or overhead water tank) using tan(θ).",
            materialsRequired = listOf(
                "Plastic 180° Protractor",
                "Straight drinking straw / hollow tube",
                "Strong adhesive tape / glue",
                "20 cm cotton thread",
                "Small metal nut or washer (plumb weight)",
                "Measuring tape (50 meters or standard 3-meter tape)"
            ),
            theoryAndPrinciple = "Trigonometric Right Triangle Principle:\nLet distance from observer to base of building = d.\nLet observer's eye height = h_eye.\nAngle of elevation measured by clinometer = θ.\n\ntan(θ) = Opposite / Adjacent = (H - h_eye) / d\n=> H = d × tan(θ) + h_eye.",
            stepByStepProcedure = listOf(
                "1. Fasten the drinking straw firmly along the flat straight edge (0°-180° diameter) of the protractor.",
                "2. Tie a small weight (nut) to one end of the 20cm thread.",
                "3. Secure the other end of the thread exactly through the central vertex origin of the protractor.",
                "4. Step a measured horizontal distance 'd' (e.g. 15 meters) away from the school wall.",
                "5. Sight top of building through straw. The hanging thread gives the angle (90° - reading).",
                "6. Multiply distance d by tan(θ) and add your eye level height h_eye to obtain total height H."
            ),
            observationTable = listOf(
                "Eye level height (h_eye)" to "1.50 meters",
                "Distance from building (d)" to "15.00 meters",
                "Angle of elevation (θ)" to "45 degrees (tan 45° = 1.000)",
                "Computed Height H" to "15.00 × 1.000 + 1.50 = 16.50 meters",
                "Verification with 30° at d=26m" to "26.0 × tan(30°) + 1.5 = 26.0 × 0.577 + 1.5 = 16.50 m"
            ),
            calculations = "H = d × tan(θ) + h_eye\nFor θ = 45°: tan(45°) = 1.000\nH = 15.00 × 1.000 + 1.50 = 16.50 m.",
            vivaQuestions = listOf(
                "Why is 45° the most convenient angle of elevation to measure?" to "Because tan(45°) = 1, meaning the height above eye level is equal to the distance walked from the base!",
                "What is the difference between angle of elevation and angle of depression?" to "Angle of elevation is above horizontal looking up; angle of depression is below horizontal looking down."
            ),
            boardSubmissionFormat = "Board Project Portfolio: Objective -> Construction Procedure -> Mathematical Proof -> Real Data Collection -> Error Analysis.",
            isCompleted = true
        ),
        EducationalProject(
            id = "proj_onion_cell_mount",
            title = "Plant Cell Morphology: Temporary Mount of Onion Epidermal Peel",
            subject = "Biology Practical",
            grade = "Class 9 & 11 (Cytology Lab)",
            durationText = "35 mins Microscopy",
            practicalCategory = "Biology Study",
            objective = "Prepare a temporary stained mount of onion bulb epidermal peel to observe cell wall, distinct nucleus, cytoplasm, and vacuole under a compound microscope.",
            materialsRequired = listOf(
                "Fresh onion bulb",
                "Watch glass with distilled water",
                "Fine forceps, dissecting needle, and sharp blade",
                "Safranin stain (dilute)",
                "Glycerine (mounting medium)",
                "Clean glass slides and glass coverslips",
                "Filter paper strip for blotting",
                "Compound Light Microscope (10x & 40x objectives)"
            ),
            theoryAndPrinciple = "Plant cells have a rigid cellulose cell wall outside the plasma membrane and prominent central vacuoles. Safranin stains lignified and cutinized cell components and nuclei bright pink/red for optical contrast under light microscopy.",
            stepByStepProcedure = listOf(
                "1. Cut a fleshy scale leaf from an onion and gently peel the thin transparent inner epidermal layer using forceps.",
                "2. Immediately place peel in a watch glass containing water to prevent dehydration.",
                "3. Transfer peel to a watch glass with 2 drops of Safranin stain for 2 minutes.",
                "4. Wash excess stain gently in clean water.",
                "5. Place stained peel flat on center of slide; add a drop of pure glycerine.",
                "6. Gently lower coverslip at 45° angle with needle to avoid trapped air bubbles.",
                "7. Focus under 10x scanning power, then switch to 40x high power."
            ),
            observationTable = listOf(
                "Cell Shape" to "Compact rectangular brick-like cells arranged without intercellular spaces.",
                "Outer Boundary" to "Distinct rigid dark-stained double line (Cell Wall).",
                "Nucleus" to "Prominent, darkly stained spherical body pushed towards cell periphery.",
                "Cytoplasm & Vacuole" to "Thin granular cytoplasm lining the wall; large central clear vacuole."
            ),
            calculations = "Total Magnification = Eyepiece (10x) × Objective (40x) = 400x enlargement.",
            vivaQuestions = listOf(
                "Why is glycerine used for mounting instead of water?" to "Glycerine prevents the biological specimen from drying out under the heat of the microscope lamp and matches the refractive index of glass.",
                "Why is the nucleus situated at the periphery in plant cells?" to "Because of the large central vacuole that occupies 80-90% of the cell volume, pushing the nucleus to one side."
            ),
            boardSubmissionFormat = "Biology Practical Journal: Aim -> Principle -> Procedure -> Neatly Labeled Diagram -> Diagnostic Characteristics.",
            isCompleted = false
        ),
        EducationalProject(
            id = "proj_stem_python_solver",
            title = "STEM Computing: Python Quadratic Root Engine & Discriminant Analyzer",
            subject = "Computer Science & Math Project",
            grade = "Class 10-12 (Coding & STEM)",
            durationText = "50 mins Computing Lab",
            practicalCategory = "STEM Coding",
            objective = "Implement an algorithmic solver in Python that accepts quadratic coefficients a, b, c, determines discriminant D = b² - 4ac, classifies roots, and calculates exact vertex and roots.",
            materialsRequired = listOf(
                "Computer / Android device with Python runner",
                "Text editor / IDE",
                "Math module (math.sqrt)",
                "Test cases dataset (Real distinct, Equal, Imaginary)"
            ),
            theoryAndPrinciple = "Shridharacharya formula:\nx = (-b ± √(b² - 4ac)) / (2a)\nDiscriminant D = b² - 4ac:\n- D > 0: Two distinct real roots\n- D == 0: Two equal real roots (-b / 2a)\n- D < 0: Complex conjugate roots (-b/2a ± i√|D|/2a).",
            stepByStepProcedure = listOf(
                "1. Import math module: import math, cmath.",
                "2. Define quadratic_solver(a, b, c) function with zero-division check on 'a'.",
                "3. Compute D = b**2 - 4*a*c.",
                "4. Use conditional branching (if/elif/else) for D > 0, D == 0, D < 0.",
                "5. Calculate vertex coordinates (-b/(2a), -D/(4a)) for parabola trajectory.",
                "6. Format output with 3 decimal precision and print verification equation."
            ),
            observationTable = listOf(
                "Test Case 1: x² - 5x + 6 = 0" to "D = 1 > 0 => Real Distinct: x1 = 3.0, x2 = 2.0",
                "Test Case 2: x² - 6x + 9 = 0" to "D = 0 => Real Equal: x1 = x2 = 3.0",
                "Test Case 3: x² + 2x + 5 = 0" to "D = -16 < 0 => Complex Roots: -1.0 ± 2.0i",
                "Vertex Coordinates for (1, -5, 6)" to "Vertex at (2.500, -0.250) Minimum Point"
            ),
            calculations = "Time complexity: O(1) constant time arithmetic.\nSpace complexity: O(1).",
            vivaQuestions = listOf(
                "What runtime error occurs if the user inputs a = 0?" to "ZeroDivisionError in 2*a, and the equation degenerates into a linear equation (bx + c = 0).",
                "How does the vertex relate to projectile motion in physics?" to "The vertex (-b/2a, -D/4a) represents the maximum height and time of flight for a ball thrown upward under gravity!"
            ),
            boardSubmissionFormat = "STEM Project Submission: Problem Statement -> Algorithm -> Source Code -> Execution Test Cases -> Practical Applications.",
            isCompleted = true
        )
    )
}
